(ns sierpinski.core
  (:gen-class)
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [sierpinski.triangle-builder :as t]
            [sierpinski.new-triangle-builder :as nt]))
(defn setup []
  (q/frame-rate 60)
  {:tri-size (/ (q/screen-height) 3)
   :depth    7})

(defn depth-by-size [size]
  (min (int (/ size 70)) 8))

(defn update-state [state]
  (assoc state :depth (depth-by-size (:tri-size state))))

(defn zoom-factor [size]
  (max (int (/ size 200)) 1))

(defn mouse-wheel [state event]
  (let [zoom-inc (* event (zoom-factor (:tri-size state)))]
    (update-in state [:tri-size] #(+ zoom-inc %))))

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

(defn is-point? [[[_ ay] [_ by] _]]
  (= ay by))

(defn keep-min-size [l] l
  (remove is-point? l))

(defn draw-state [state]
  (q/background 255)
  (q/fill 255 255)
  (let [xmiddle (/ (q/width) 2)
        ymiddle (/ (q/height) 2)
        start-triangle (t/get-triangle (:tri-size state) xmiddle ymiddle)
        triangles (t/finite-triangles (:depth state) start-triangle)]
    (-> triangles
        keep-inbounds
        keep-min-size
        draw-triangles-in-list)))

(defn new-draw-state [state]
  (q/background 255)
  (q/fill 255 255)
  (let [triangles (nt/just-enough-triangles [10 (- (q/height) 10)] 10 (:tri-size state))]
    (doall (map #(apply q/triangle (flatten %)) triangles))))

(defn -main [& args]
  (q/defsketch sierpinski
               :title "Zooming Sierpinsky Triangle"
               :size [(- (q/screen-width) 100) (- (q/screen-height) 150)]
               ; setup function called only once, during sketch initialization.
               :setup setup
               ; update-state is called on each iteration before draw-state.
               :update update-state
               :mouse-wheel mouse-wheel
               :draw new-draw-state
               ;:features [:exit-on-close]
               ; This sketch uses functional-mode middleware.
               ; Check quil wiki for more info about middlewares and particularly
               ; fun-mode.
               :middleware [m/fun-mode]))
