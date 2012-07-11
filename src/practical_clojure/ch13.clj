;;; Chapter 13 - Datatypes and Protocols
(ns practical-clojure.ch13)

;; Protocol looks like a Java interface. A protocol is a set of
;; methods grouped together.

(defprotocol my-protocol
  "This is my own protocol"
  (method-one [x] "This is the first method.")
  (method-two [x y] "This is the second method."))

;; Protocols do not have inheritance.

;; Datatypes. This is what defrecord does. It is like a dumb data
;; object in Java.
(defrecord Employee [name room])

(def hans (Employee. "hans" 35))
(:name hans)
(:room hans)

;; defrecord is faster than map lookup because we say the names to
;; clojure and then clojure can optimise.

;; You can treat a defrecord like a map with assoc and dissoc
(:pet (assoc hans :pet "shanti"))
(dissoc (assoc hans :pet "shanti") :pet)

;; If you dissoc a defrecord field you get a plain map.
(dissoc hans :name)

;; You can combine defprotocol with defrecord
(defrecord Dwarf [name skill]
  my-protocol
  (method-one [x] "I am dwarf")
  (method-two [x y] (println x)))

;; Reify is like anonymous fn, but for things
(def thing (let [s "I am captured!"]
             (reify java.lang.Object
               (toString [this] s))))
(println (str thing))

;;; Payroll example to bring it all together.
(defprotocol Payroll
  "Calculate paycheck for an employ based on worked hours." 
  (paycheck [emp hrs]))

(defrecord HourlyEmployee [name rate]
  Payroll
  (paycheck [emp hrs]  (* rate hrs)))

(defrecord SalariedEmployee [name salary]
  Payroll
  (paycheck [emp hrs] (/ salary 12.0)))

(defn contract [amount]
  "Fixed bid contractor."
  (reify Payroll
    (paycheck [emp hrs] amount)))

(def con1 (contract 5000))
(paycheck con1 5)

;;; Advanced datatypes. We have deftype, a low level tool. It is like
;;; defrecord but without the automatic methods and not event the
;;; standard java methods.

