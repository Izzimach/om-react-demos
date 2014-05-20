(ns omreactdemos.counterapp
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [goog.events :as events]
            [cljs.core.async :as async :refer [>! <! put! chan sliding-buffer]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true])
  (:import [goog.events EventType])
  )


(def counter-app-state (atom {:counters (into [] (map (fn [n] {:id n :count 0}) (range 0 10)))}))

(defn counter [cursor owner]
  (reify
    om/IRenderState
    (render-state [_ {:keys [last-clicked]}]
                  (dom/div nil
                           (dom/label nil (:count cursor))
                           (dom/button
                            #js {:onClick
                                 (fn [e]
                                   (om/transact! cursor :count inc)
                                   (put! last-clicked (.-path cursor)))}
                            "+")
                           (dom/button
                            #js {:onClick
                                 (fn [e]
                                   (om/transact! cursor :count dec)
                                   (put! last-clicked (.-path cursor)))}
                            "-")))))

(defn counter-view [appdata owner]
  (reify
    om/IInitState
    (init-state [_]
                {:chans {:last-clicked (chan (sliding-buffer 1))}})
    om/IWillMount
    (will-mount [_]
                (let [last-clicked (om/get-state owner [:chans :last-clicked])]
                  (go (while true
                        (let [lc (<! last-clicked)]
                          (om/set-state! owner :message lc))))))
    om/IRenderState
    (render-state [_ {:keys [message chans]}]
                  (apply dom/div nil
                         (dom/h1 #js {:key "head"} "A Counting Widget!")
                         (dom/div
                          #js {:key "message"
                               :style
                               (if message
                                 #js {:display "block"}
                                 #js {:display "none"})}
                          (when message
                            (str "Last clicked item was " (last message))))
                         (om/build-all counter (:counters appdata)
                                       {:key :id :init-state chans})))))

(defn startcounterapp [elementid]
  (om/root counter-view counter-app-state
           {:target (.getElementById js/document elementid)
              :tx-listen (fn [tx-data root-cursor] (println tx-data))}))

