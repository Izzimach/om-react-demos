(ns omreactdemos.tilesapp
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [goog.events :as events]
            [cljs.core.async :as async :refer [>! <! put! chan sliding-buffer]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true])
  (:import [goog.events EventType]))

(defn maketile [id] {:id id :text "arrrrgh!" })

(def tilesapp-state (atom {:tiles (map maketile [1 2 3 4])}))

(defn singletile [propscursor owner]
  (reify
    om/IInitState
    (init-state [_]
                {:stuff "Argh!"}
                )
    om/IRenderState
    (render-state [_ state]
                  (dom/div #js {:style #js {:width "150px" :display "inline-block"}} (:text propscursor)))))

(defn tilegroup [cursor]
    (om/component
     (apply dom/div nil
            (om/build-all singletile (:tiles cursor)))))

(defn starttilesapp [elementid]
  (om/root tilegroup tilesapp-state
           {:target (.getElementById js/document elementid)}))


