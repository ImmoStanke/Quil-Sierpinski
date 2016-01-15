(ns sierpinski.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [sierpinski.triangle-builder :as t]))

(defn setup []
  (q/frame-rate 30)
  {:tri-size 250
   :depth 2
   :frame 1})


(defn update-size [size]
  (let [actual (- size 250)] 
    (+ 250
       (mod 
        (+
         actual
         actual)

        500)))
)

(defn update-state [state]
  {:tri-size (update-size (:tri-size state))
   :depth (+ 2 (mod  (if (= 29 (:frame state)) (inc (:depth state)) (:depth state)) 5))
   :frame (mod (inc (:frame state)) 30)
}
  ;{:tri-size 50}
  )



  (defn draw-nested-triangle [n old-coordinates]
    (if (> n 0)

      (let [new-coords (map t/line-middle old-coordinates (rest (take 4 (cycle old-coordinates))))
            left-tri [(first new-coords) (second old-coordinates) (second new-coords)]
            mid-tri [(first old-coordinates) (first new-coords) (last new-coords)]
            right-tri [(last new-coords) (second new-coords) (last old-coordinates)]
            ]

        (apply q/triangle (apply concat old-coordinates))

        (draw-nested-triangle (- n 1) left-tri)
        (draw-nested-triangle (- n 1) mid-tri)
        (recur (- n 1) right-tri)

        )
      )
    )

(defn draw-triangles-in-list 
([] nil)
([tri-list]
 (when-not (empty? tri-list)
   (apply q/triangle (apply concat (first tri-list)))
   (recur (rest tri-list)))))


(defn draw-state [state]
  (q/background 255)
  (q/fill 255 255)
  
  (draw-triangles-in-list (t/finite-triangles 7 (t/get-triangle (:tri-size state) 250 250)) )
  )
  (q/defsketch sierpinski
               :title "Zooming Sierpinsky Triangle"
               :size [500 500]
               ; setup function called only once, during sketch initialization.
               :setup setup
               ; update-state is called on each iteration before draw-state.
               :update update-state
               :draw draw-state
               :features [:keep-on-top]
               ; This sketch uses functional-mode middleware.
               ; Check quil wiki for more info about middlewares and particularly
               ; fun-mode.
               :middleware [m/fun-mode])
