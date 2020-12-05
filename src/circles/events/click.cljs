(ns circles.events.click
  (:require [circles.event :as event]))

(defn origin [canvas]
  {:x (+ (.-offsetLeft canvas) (.-clientLeft canvas))
   :y (+ (.-offsetTop canvas) (.-clientTop canvas))})

(defn position [canvas event]
  (let [origin (.getBoundingClientRect canvas)]
    {:x (- (.-pageX event) (.-left origin))
     :y (- (.-pageY event) (.-top origin))}))

(defn make [click canvas events]
  (assoc (position canvas click)
         :event :click
         :id (event/next-id events)))

