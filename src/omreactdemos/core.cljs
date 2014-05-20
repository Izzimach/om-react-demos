(ns omreactdemos.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [goog.events :as events]
            [cljs.core.async :as async :refer [>! <! put! chan sliding-buffer]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [omreactdemos.helloworld :refer [starthelloworld debughelloworld]]
            [omreactdemos.mouselistener :refer [startmouseapp]]
            [omreactdemos.tilesapp :refer [starttilesapp]]
            [omreactdemos.counterapp :refer [startcounterapp]]

            )
  (:import [goog.events EventType])
  )

#_(enable-console-print!)

(println "Hello world!")

(starthelloworld
   {:text "Hello world one!" :debugtext "blargh!"}
   "my-app")

#_(omreactdemos.helloworld.debughelloworld
 {:text "Hello world one!" :debugtext "blargh!"}
 "my-app2")


#_(startmouseapp "my-app")
#_(starttilesapp "my-app")
#_(startcounterapp "my-app")
#_(debugcounterstate "my-app2")






