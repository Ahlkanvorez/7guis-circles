(ns circles.events.scale)

(defn make [circle-id old-radius new-radius]
  {:event :scale
   :circle-id circle-id
   :from old-radius
   :to new-radius})
