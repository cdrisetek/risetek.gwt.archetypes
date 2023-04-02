package ${package}.utils;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ScriptElement;

import java.util.LinkedList;

public class ExternalJavaScriptLoader
{
   public interface Callback
   {
      void onLoaded();
   }

   private enum State
   {
      Start,
      Loading,
      Loaded,
      Error
   }

   public static void loadSequentially(String[] urls, final Callback callback)
   {
      final LinkedList<ExternalJavaScriptLoader> loaders = new LinkedList<>();

      for (String url : urls)
         loaders.add(new ExternalJavaScriptLoader(url));

      Callback innerCallback = new Callback()
      {
         public void onLoaded()
         {
            if (!loaders.isEmpty())
               loaders.remove().addCallback(this);
            else
               callback.onLoaded();
         }
      };
      innerCallback.onLoaded();
   }

   public ExternalJavaScriptLoader(String url)
   {
      this(Document.get(), url);
   }

   public ExternalJavaScriptLoader(Document document, String url)
   {
      document_ = document;
      url_ = url;
   }

   public boolean isLoaded()
   {
      return state_ == State.Loaded;
   }

   public void addCallback(Callback callback)
   {
      switch (state_)
      {
         case Start:
            callbacks_.add(callback);
            startLoading();
            break;
         case Loading:
            callbacks_.add(callback);
            break;
         case Loaded:
            callback.onLoaded();
            break;
         case Error:
            break;
      }
   }

   private void startLoading()
   {
      assert state_ == State.Start;
      state_ = State.Loading;
      ScriptElement script = document_.createScriptElement();
      script.setType("text/javascript");
      script.setSrc(url_);
      registerCallback(script);
      Element head = document_.getElementsByTagName("head").getItem(0);
      head.appendChild(script);
   }

   private native void registerCallback(ScriptElement script) /*-{
      var self = this;
      script.onreadystatechange = $entry(function() {
         if (this.readyState == 'complete')
            self.@${package}.utils.ExternalJavaScriptLoader::onLoaded()();
      });
      script.onload = $entry(function() {
         self.@${package}.utils.ExternalJavaScriptLoader::onLoaded()();
      });
   }-*/;

   private void onLoaded()
   {
      state_ = State.Loaded;
      Scheduler.get().scheduleIncremental(new RepeatingCommand()
      {
         public boolean execute()
         {
            if (!callbacks_.isEmpty())
               callbacks_.remove().onLoaded();

            return !callbacks_.isEmpty();
         }
      });
   }

   private LinkedList<Callback> callbacks_ = new LinkedList<>();
   private State state_ = State.Start;
   private final String url_;
   private final Document document_;
}