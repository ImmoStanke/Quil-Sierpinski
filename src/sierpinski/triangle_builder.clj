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

  (map #(/ (+ %1 %2) 2) a b)
  )

(def sub-triangles-memo (memoize sub-triangles))

 (defn sub-triangles [[a b c]]
     (let [ab (line-middle a b)
           ac (line-middle a c)
           bc (line-middle b c)]
       [[a ab ac] [ab b bc] [ac bc c]]
       )
     )

(defn- number-for-depth [depth] 
 (if (> depth 1)
   (+ (Math/pow 3 depth) (number-for-depth (dec depth)) )
   (0)
 ))

(defn finite-triangles [depth initial]

  )
                                        ;lazy seq of triangle coordinates

(defn infinite-triangles
  ([initial-triangle]
   (let [[top right left] (sub-triangles initial-triangle)]
     (infinite-triangles top right left))
   )
  ([first-triangle & remaining-triangles]
   (let [[top right left] (sub-triangles-memo first-triangle)]
                                        ; (println "recuring with" top right left)

     (lazy-seq
      (concat
       [top right left]
       (apply infinite-triangles (concat  remaining-triangles [top right left]))
       
       ))
     )
   ))

(take 10 (infinite-triangles [[100 100] [150 150] [150 50]]))
