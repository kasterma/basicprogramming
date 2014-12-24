(ns core
  (:require [clojure.tools.logging :as log]
            [clojure.test :as test]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.tools.trace :as trace]
            [criterium.core :as criterium]))

(defn get-int [] (long (Integer/parseInt (read-line))))
(defn get-ints [] (map (fn [x] (long (Integer/parseInt x)))
                       (clojure.string/split (read-line) #" ")))
(defn get-query [] ((fn [[Q a b]] [Q (long (Integer/parseInt a)) (long (Integer/parseInt b))])
                       (clojure.string/split (read-line) #" ")))

(def primes #{2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89 97})

(defn primef
  ([x]
     (primef x {}))
  ([x m]
     (cond
      (= x 1) m
      (contains? primes x) (update-in m [x] (fnil inc 0))
      :default
      (let [p  (first (filter (fn [p] (zero? (rem x p))) primes))]
        (recur (quot x p) (update-in m [p] (fnil inc 0)))))))

(defn initial-prime-facts
  ([]
     (initial-prime-facts 100))
  ([n]
     (into {}
           (for [x (range 1 n)] [x (primef x)]))))

(def prime-factorizations (atom (initial-prime-facts)))

(defn new-primef [x y]
  (merge-with + (@prime-factorizations x) (@prime-factorizations y)))

(defn update-A-fn [x b]
  (let [new-val (* x b)]
    (if (not (contains? (set (keys @prime-factorizations)) new-val))
      (swap! prime-factorizations assoc new-val (new-primef x b)))
    new-val))

(def BD 1000000007)

(defn eval-pf [pf]
  (reduce (fn [acc [p x]] (mod (* acc (bigint (Math/pow p x))) BD)) 1N pf))

(defn execute-fn [as]
  (let [pfs (map @prime-factorizations as)
        pf  (reduce (partial merge-with max) pfs)]
    pf))

(defn execute-query [A [Q a b]]
  (if (= Q "Q")
    (do (println (int (eval-pf (execute-fn (take (inc (- b a)) (drop a A))))))
        A)
    (update-in A [a] update-A-fn b)))

(defn main []
  (let [n (get-int)
        A (vec (get-ints))
        K (get-int)
        Qs (for [k (range K)] (get-query))]
    (reduce execute-query A Qs)))

;; 90
;; 30
;; 9
;; 18
;; 24
(binding [*in* (java.io.BufferedReader. (java.io.FileReader. "testcase.txt"))]
  (main))
