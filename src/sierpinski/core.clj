(ns sierpinski.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [sierpinski.triangle-builder :as t]))

(defn setup []
  (q/frame-rate 30)
  {:tri-size 250})

(defn update-size [size]
  (let [actual (- size 249)] 
    (+ 250
       (mod (+ 1 actual) 1500))))

(defn update-state [state]
  {:tri-size (update-size (:tri-size state))} )

(defn draw-triangles-in-list 
([] nil)
([tri-list]
 (when-not (empty? tri-list)
   (apply q/triangle (apply concat (first tri-list)))
   (recur (rest tri-list)))))

;[ax ay][bx by][cx cy]
(defn out-of-bounds? [[[_ highest] [rightmost lowest] [leftmost _]]]
  (or (> highest (q/height))
      (> leftmost (q/width))
      (<= lowest 0)
      (<= rightmost 0)))

(defn keep-inbounds [l] 
  (remove out-of-bounds? l))

(defn is-point? [[[_ ay] [_ by] _ ]]
 (= ay by))

(defn keep-min-size [l] l
  (remove is-point? l))

(defn draw-state [state]
  (q/background 255)
  (q/fill 255 255)
  (let [middle 250 
        start-triangle (t/get-triangle (:tri-size state) middle middle)
        triangles (t/finite-triangles 8 start-triangle)] 
    (-> triangles
     keep-inbounds
     keep-min-size
     draw-triangles-in-list )))

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
