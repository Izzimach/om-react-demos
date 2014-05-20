(ns omreactdemos.mouselistener
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [goog.events :as events]
            [cljs.core.async :as async :refer [>! <! put! chan sliding-buffer]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [clojure.string :as cstring])
  (:import [goog.events EventType]))


(defn listen [element type]
  (let [out (chan)]
    (events/listen element type #(put! out %))
    out))


(defn mouse-view [app owner]
  (reify
    om/IWillMount
    (will-mount [_]
                (let [fixcoords (fn [e] [(./-clientX e) (.-clientY e)])
                      mouse-chan (async/map fixcoords [(listen js/window EventType/MOUSEMOVE)])]
                  (go (while true
                        (om/update! app :mouse (<! mouse-chan))))))
    om/IRender
     (render [_]
             (dom/p nil
                    (when-let [pos (:mouse app)]
                      (pr-str (:mouse app)))))))

(defn mouse-key-view [app owner]
  (reify
    om/IWillMount
    (will-mount [_]
                (let [fixcoords (fn [e] [(./-clientX e) (.-clientY e)])
                      mouse-chan (async/map fixcoords [(listen js/window EventType/MOUSEMOVE)])
                      keypress-chan (listen js/window EventType/KEYPRESS)]
                  (go (while true
                        (apply om/update! app (alt!
                                         mouse-chan ([val ch] [:mouse val])
                                         keypress-chan ([val ch] [:keypress val])))))))
    om/IRender
     (render [_]
             (dom/p nil
                    (cstring/join ["Mouse: "
                                   (if-let [pos (:mouse app)] (pr-str pos) "")
                                   " Keypress: "
                                   (if-let [keypress (:keypress app)] (pr-str (.-keyCode keypress)) "")])))))

(defn startmouseapp [elementid]
  (om/root mouse-view {:mouse nil :keypress nil}
           {:target (.getElementById js/document "my-app")}))

