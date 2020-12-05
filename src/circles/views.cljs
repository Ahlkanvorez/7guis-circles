(ns circles.views)

(defn rows [width & rows]
  [:div {:style {:display :flex :flex-direction :column
                 :max-width width :min-width width}}
   (for [[idx row] (map-indexed vector rows)]
     [:div {:key idx} row])])

(defn history-buttons [{:keys [on-undo on-redo can-undo? can-redo?]}]
  [:div {:style {:display :flex :justify-content :center}}
   [:input {:style {:margin-right "15px"}
            :type :button
            :value :Undo
            :disabled (not can-undo?)
            :on-click on-undo}]
   [:input {:type :button
            :value :Redo
            :disabled (not can-redo?)
            :on-click on-redo}]])

(defn canvas [{:keys [id width height on-click]}]
  [:canvas {:style {:border "1px solid black"}
            :width width
            :height height
            :id id
            :on-click on-click}])

(defn circle-scaler [{:keys [circle on-scale]}]
  [:div {:style {:display :flex :flex-direction :column}}
   [:label "Adjust diameter of circle at (" (:x circle) ", " (:y circle) ")."]
   [:input {:type :range :min 1 :max 200 :step 1
            :value (* 2 (:radius circle))
            :on-change on-scale}]])
