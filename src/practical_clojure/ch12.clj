;;; Macros and metaprogramming
(ns practical-clojure.ch12)

(defmacro triple [form]
  (list 'do form form form))

(defmacro triple-ex [form]
  `(do ~form ~form ~form))

(defmacro zuba [seq]
  `(println ~@seq))


;; macro to choose between 2 alternatives randomly
(defmacro random-action [a b]
  `(if (zero? (rand-int 2))
     ~a ~b))

;; macro to choose multi
(defmacro random-multi [& exprs]
  `(let [ct# (count ~exprs)]
     (case (rand-int ct#)
       ~(interleave (range (count exprs)) exprs))))

;; You can also use recursion in macros. Lets make a macro for ++ to
;; add multiple items.
(defmacro ++ [& exprs]
  (if (>= 2 (count exprs))
    `(+ ~@exprs)
    `(+ ~@(first exprs) (++ ~@(rest exprs)))))

;; Macros make DSL simple
(defn xml-helper [form]
  (if (not (seq? form))
    (str form)
    (let [name (first form)
          children (rest form)]
      (str "<" name ">"
           (apply str (map xml-helper children))
           "</" name ">"))))

(defmacro xml [form]
  (xml-helper form))

(xml
 (book
  (authors
   (author "Steve")
   (author "Dave"))))