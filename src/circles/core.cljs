(ns circles.core
  (:require [reagent.core :as r]
            [reagent.dom :as rd]))

(defn circle-drawer []
  [:div {:style {:display :flex :flex-direction :column}}
   [:div {:style {:display :flex :justify-content :center}}
    [:input {:type :button :value :Undo :style {:margin-right "15px"}}]
    [:input {:type :button :value :Redo}]]
   [:canvas {:style {:flex "1 1" :border "1px solid black"}
             :width 500 :height 500
             :id "circles-canvas"}]])

(defn mount-root []
  (rd/render [circle-drawer]
             (js/document.getElementById "app-root")))
