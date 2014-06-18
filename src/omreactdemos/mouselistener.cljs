(ns omreactdemos.mouselistener
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [goog.events :as events]
            [cljs.core.async :as async :refer [>! <! put! chan sliding-buffer]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [clojure.string :as cstring])
  (:import [goog.events EventType]))


(def mouselistener-state (atom {:mouse nil :keypress nil}))


(defn listen [element type]
  "Returns a channel that emits mouse events"
  (let [out (chan)]
    (events/listen element type #(put! out %))
    out))

(defn localmousecoords [event]
  "Get local coords of the mouse event"
  [(./-clientX event) (.-clientY event)])

(defn mouse-view [app owner]
  (reify
    om/IWillMount
    (will-mount [_]
                (let [mouse-chan (async/map localmousecoords [(listen js/window EventType/MOUSEMOVE)])]
                  (go (while true
                        (om/update! app :mouse (<! mouse-chan))))))
    om/IRender
     (render [_]
             (dom/p nil
                    (when-let [pos (:mouse app)]
                      (pr-str (:mouse app)))))))

(defn key-view [app owner]
  (reify
    om/IWillMount
    (will-mount [_]
                ;; just grab all keypress events
                (let [keypress-chan (listen js/window EventType/KEYPRESS)]
                  (go (while true
                        (om/update! app :keypress (<! keypress-chan))))))
    om/IRender
     (render [_]
             (dom/p nil
                    (when-let [keyevent (:keypress app)]
                      (pr-str (.-keyCode keyevent)))))))

(defn mouse-key-view [app owner]
  (dom/div nil (om/build mouse-view app) (om/build key-view app)))

(defn startmouseapp [elementid]
  (om/root mouse-view mouselistener-state
           {:target (.getElementById js/document elementid)}))

(defn startkeypressapp [elementid]
  (om/root mouse-key-view mouselistener-state
           {:target (.getElementById js/document elementid)}))
