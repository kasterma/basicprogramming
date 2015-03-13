(ns core
  (:require [taoensso.timbre :as log]
            [taoensso.timbre.profiling :as prof]
            [clojure.test :as test]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.tools.trace :as trace]
            [criterium.core :as criterium]))

(use 'clojure.repl)

(defn get-int [] (long (Integer/parseInt (read-line))))
(defn get-ints [] (map (fn [x] (long (Integer/parseInt x)))
                       (clojure.string/split (read-line) #" ")))


;;(trace/trace-vars step)

(defn get-max [floor-cts I]
  (let [ground-floors (mapv (comp vector first) floor-cts)
        num-bld       (count floor-cts)
        new-val
        (fn [acc building floor]
         (if (<= I floor) ;; can jump down I
          (+ (get-in floor-cts [building floor])
                (loop [max-sofar (get-in acc [building (dec floor)])
                       b 0]
                 (if (= b num-bld)
                  max-sofar
                  (if (= b building)
                    (recur max-sofar (inc b))
                    (recur (max max-sofar (get-in acc [b (- floor I)])) (inc b))))))
          (+ (get-in floor-cts [building floor])
             (get-in acc [building (dec floor)]))))
        add-floors
        (fn [acc f]
          (reduce (fn [acc b]
                     (prof/p :ui (assoc!
                      acc
                      b
                      (conj (get acc b) (prof/p :nv (new-val acc b f))))))
                  acc
                  (range (count floor-cts))))
        ]
    ;;(println floor-cts)
    (persistent! (reduce add-floors
      (transient ground-floors)
      (range 1 (count (first floor-cts))) ;; start at 1 since already have ground floor
      ))))

(defn main []
  (let [[N H I]      (get-ints)
        floor-info   (prof/p :read (doall (for [i (range N)] (vec (rest (get-ints))))))
        building-cts (fn [idxs]
                       (persistent!
                        (reduce
                         (fn [acc idx]
                          (assoc! acc (dec idx) (inc (get acc (dec idx)))))
                         (transient (vec (repeat H 0)))
                         idxs)))
        floor-cts    (prof/p :floors (mapv building-cts floor-info))]
  ;;(println [N H I floor-info floor-cts])
  ;;(println (get-max floor-cts I))
  (println (prof/p :max (apply max (map last (get-max floor-cts I)))))))
;; 2
;; 3
(binding [*in* (java.io.BufferedReader. (java.io.FileReader. "testcase.txt"))]
  (prof/profile :info :xx (main)))
