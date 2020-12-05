(ns circles.canvas
  (:require [circles.circle :as circle]))

(defn clear [canvas]
  (when-let [ctx (.getContext canvas "2d")]
    (set! (.-fillStyle ctx) "rgb(255, 255, 255)")
    (.fillRect ctx 0 0 (.-width canvas) (.-height canvas))))

(defn selected-circle [circles point]
  (first (filter (partial circle/contains-point? point) circles)))

(defn draw [canvas db]
  (clear canvas)
  (doseq [circle (vals (:circles db))]
    (circle/draw canvas circle (:selected db))))
