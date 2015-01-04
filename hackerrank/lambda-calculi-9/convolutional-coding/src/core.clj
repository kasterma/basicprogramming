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
(defn main []
  (let [n (get-int)]
    (println n)))

(def s1 [1 1 0 1 0 1 0 0])

(defn shift-register-map
  [f reg s]
  (let [val (f reg)]
    (if (empty? s)
      (list val)
      (cons val
            (lazy-seq (shift-register-map f
                                          (conj (vec (rest reg)) (first s))
                                          (rest s)))))))

(shift-register-map identity [0 0] s1)

(defn make-poly [spec]
  (let [sp (reverse spec)]
    (fn [reg]
      (mod (apply + (map (fn [x y] (* x y)) reg sp))
           2))))

(defn code-map [poly-specs]
  (let [poly-fns (map make-poly poly-specs)]
    (fn [reg]
      (mapv (fn [f] (f reg)) poly-fns))))

(defn encoding
  [poly-specs reg-size s]
  (clojure.string/join ""
                       (flatten (rest (shift-register-map (code-map poly-specs)
                                                          (vec (repeat reg-size 0))
                                                          (concat s (repeat reg-size 0)))))))

(encoding [[1,1,1,1,0,0,1] [1,0,1,1,0,1,1]] 7 [0 1 1 0 1 0 0 0 0 1 1 0 1 0 0 1])
;; => "0011010111011001111010011101101001100000011100"

;; 2
;; 3
#_(binding [*in* (java.io.BufferedReader. (java.io.FileReader. "testcase.txt"))]
  (main))

(defn prep-input [N s]
  (partition N (map (comp read-string str) (seq s))))

(prep-input 3 "101110")

(defn hamming [v1 v2]
  (count (filter (fn [[x y]] (not (= x y)))
                 (map list v1 v2))))



(defn decode [message K N specs]
  (let [m        (prep-input N message)
        enc      (code-map specs)
        decode_h (fn dec_h [[state epoch]]
                   (cond
                    (= epoch 0)
                    (if (contains? #{[0 0]} state)
                      [[] 0]
                      [[] Integer/MAX_VALUE])

                    (and (= epoch 1) (contains? #{[1 0] [1 1]} state))
                    [[] Integer/MAX_VALUE]

                    :default
                    (let [[p0 d0] (dec_h [(into [0] (drop-last state)) (dec epoch)])
                          [p1 d1] (dec_h [(into [1] (drop-last state)) (dec epoch)])
                          d       (hamming (enc state) (nth m (dec epoch)))]
                      (if (< d0 d1)
                        [(conj p0 state) (+ d0 d)]
                        [(conj p1 state) (+ d1 d)]))))
        states   (decode_h [(vec (repeat K 0)) (count m)])]
    (clojure.string/join (map last (drop-last 2 (first states))))))

(def testmessage "01101110011100")
(def m (prep-input 2 testmessage))
(def enc (code-map [[0 1] [1 1]]))
(decode testmessage 2 2 [[0 1] [1 1]])

;; testsolution: 11001
