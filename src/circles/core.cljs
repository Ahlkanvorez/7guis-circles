(ns circles.core
  (:require [reagent.core :as r]
            [reagent.dom :as rd]))

(defn mount-root []
  (rd/render [:h1 "Circles"]
             (js/document.getElementById "app-root")))
