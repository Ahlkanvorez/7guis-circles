(ns circles.circle)

(defn make-circle [center radius]
  (doto (js/Path2D.)
    (.arc (:x center) (:y center) radius 0 (* 2 js/Math.PI) true)))

(defn with-radius [radius circle]
  (dissoc
   (assoc circle
          :radius radius
          :path (make-circle circle radius))
   :event))

(defn event->circle [event]
  (with-radius 10 event))

(defn square [x] (* x x))

(defn contains-point? [point circle]
  (let [x2 (square (- (:x circle) (:x point)))
        y2 (square (- (:y circle) (:y point)))
        r2 (square (:radius circle))]
    (<= (+ x2 y2) r2)))

(defn draw [canvas circle selected-id]
  (when-let [ctx (.getContext canvas "2d")]
    (when (= (:id circle) selected-id)
      (set! (.-fillStyle ctx)  "gray")
      (.fill ctx (:path circle)))
    (set! (.-strokeStyle ctx) "black")
    (.stroke ctx (:path circle))))

