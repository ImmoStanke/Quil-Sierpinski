(ns sierpinski.triangle-builder
(:import [java.lang.Math]))


; a
;c b
;coordinate format [[ax ay][bx by][cx cy]]
(defn get-triangle [size xmiddle ymiddle]
  [[xmiddle (- ymiddle size)]
   [(+ xmiddle size) (+ ymiddle size)]
   [(- xmiddle size) (+ ymiddle size)]])

(defn line-middle
  [a b]
  (map #(int (/ (+ %1 %2) 2)) a b) )

(def sub-triangles-memo (memoize sub-triangles))

 (defn sub-triangles [[a b c]]
     (let [ab (line-middle a b)
           ac (line-middle a c)
           bc (line-middle b c)]
       [[a ab ac] [ab b bc] [ac bc c]]))

(defn- number-for-depth [depth] 
 (if (> depth 1)
   (+ (Math/pow 3 depth) (number-for-depth (dec depth)) )
   0))

(defn finite-triangles [depth initial]
  (drop 
   (number-for-depth (dec depth)) 
   (take (number-for-depth depth) (infinite-triangles initial))))
                              
(defn infinite-triangles
  ([initial-triangle]
   (apply infinite-triangles (sub-triangles initial-triangle)))
  ([first-triangle & remaining-triangles]
   (let [inner-tri (sub-triangles first-triangle)]
     (lazy-seq
      (concat
       inner-tri
       (apply infinite-triangles (concat remaining-triangles inner-tri)))))))
