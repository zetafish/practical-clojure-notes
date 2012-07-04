(ns practical_clojure.newton)

;; Recursively find square root
(defn sqrtt
  "Find the square root using Newton's method."
  ([number] (sqrt number 1.0))
  ([number guess]
     (if (good-enough number guess)
       guess
       (sqrt (avg guess (/ number guess))))))

(defn good-enough
  [number guess]
  (let [diff (- number (* guess guess))]
    (< (abs diff) 0.0001)))

(defn abs
  [number]
  (if (< number 0)
    (* -1 number)
    number))

(defn avg
  [a b]
  (/ (+ a b) 2))