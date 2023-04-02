package ${package}.utils;


import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LinkElement;

import java.util.LinkedList;

public class ExternalStyleSheetLoader
{
   public static interface Callback
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

   public ExternalStyleSheetLoader(String url)
   {
      this(Document.get(), url);
   }

   public ExternalStyleSheetLoader(Document document, String url)
   {
      document_ = document;
      url_ = url;
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
      LinkElement style = document_.createLinkElement();
      style.setType("text/css");
      style.setRel("stylesheet");
      style.setHref(url_);
      registerCallback(style);
      Element head = document_.getElementsByTagName("head").getItem(0);
      head.appendChild(style);
   }

   private native void registerCallback(LinkElement styleLink) /*-{
      var self = this;
      styleLink.onreadystatechange = $entry(function() {
         if (this.readyState == 'complete')
            self.@${package}.utils.ExternalStyleSheetLoader::onLoaded()();
      });
      styleLink.onload = $entry(function() {
         self.@${package}.utils.ExternalStyleSheetLoader::onLoaded()();
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