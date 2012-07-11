(ns practical-clojure.ch9)

;;; Multimethods and hierarchy

;; You create multimethod with defmult and you implement it with
;; defmethod.

(def a {:name "Arthur" :species ::human :strength 8})
(def b {:name "Balfor":species ::elf :strength 7})
(def c {:name "Calis", :species ::elf, :strength 5})
(def d {:name "Drung", :species ::orc, :strength 6})

(defmulti move :species)

(defmethod move ::elf [creature]
  (str (:name creature) " runs swiftly"))

(defmethod move ::human [creature]
  (str (:name creature) " walks steadily"))

(defmethod move ::orc [creature]
  (str (:name creature) " stomps heavily"))

(defmulti attack (fn [creature]
                   (if (> (:strength creature) 6)
                     :strong
                     :weak)))

(defmethod attack :strong [creature]
  (str (:name creature) " attacks heaviliy."))

(defmethod attack :weak [creature]
  (str (:name creature) " attacks flimsy."))

(defmulti encounter (fn [x y]
                      [(:species x) (:species y)]))

(defmethod encounter [::elf ::orc] [x y]
  (str "Brave " (:name x)
       " attacks evil " (:name y)))

(defmethod encounter :default [x y]
  (str (:name x) " and " (:name y) " ignore each other."))