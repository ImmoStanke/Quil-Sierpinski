(ns sierpinski.new-triangle-builder
  (:gen-class)
  (:import [java.lang.Math]))

; c
;a b
;coordinate format [[ax ay][bx by][cx cy]]
(defn get-triangle [size xmiddle ymiddle]
  [[(+ xmiddle size) (+ ymiddle size)]
   [(- xmiddle size) (+ ymiddle size)]
   [xmiddle (- ymiddle size)]])

(defn add-point [[ax ay] [bx by]]
  [(+ ax bx) (+ ay by)])

(defn translate-triangle [trans-vector triangle]
  (map #(add-point % trans-vector) triangle))

(defn line-middle
  [a b]
  (map #(int (/ (+ %1 %2) 2)) a b))

(defn sub-triangles [[a b c]]
  (let [ab (line-middle a b)
        ac (line-middle a c)
        bc (line-middle b c)]
    [[a ab ac] [ab b bc] [ac bc c]]))

(defn- number-for-depth [depth]
  (if (> depth 1)
    (+ (Math/pow 3 depth) (number-for-depth (dec depth)))
    0))

(defn just-enough-triangles
  ([left-down-corner initial-size target-size]
   (let [first-triangle [left-down-corner
                         (add-point left-down-corner [initial-size 0])
                         (add-point left-down-corner [(/ initial-size 2) (* -1 initial-size)])]]
     (if (< initial-size target-size)
       (just-enough-triangles left-down-corner initial-size target-size [first-triangle])
       [first-triangle])))
  ([left-down-corner current-size target-size previous-triangles]
   (if (< current-size target-size)
     (let [right-translate (partial translate-triangle [current-size 0])
           right-triangles (map right-translate previous-triangles)
           up-translate (partial translate-triangle [(/ current-size 2) (* -1 current-size)])
           up-triangles (map up-translate previous-triangles)
           new-triangles (concat previous-triangles right-triangles up-triangles)]
       (recur left-down-corner (* 2 current-size) target-size new-triangles)
       )
     previous-triangles)))
