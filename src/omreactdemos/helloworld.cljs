(ns omreactdemos.helloworld
  (:require [goog.events :as events]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(defn widget [cursor]
  (om/component
   (dom/div nil (:text cursor))))

(defn debug-widget [cursor]
  (om/component
   (dom/div nil (:debugtext cursor))))

(defn starthelloworld [appstate elementid]
  (om/root widget appstate
           {:target (.getElementById js/document elementid)}))

(defn debughelloworld [appstate elementid]
  (om/root debug-widget appstate
           {:target (.getElementById js/document elementid)}))

