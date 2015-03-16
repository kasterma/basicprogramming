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

(defn get-base-tree
  "Iteratively read pairs to add to tree and build the tree."
  [N]
  (loop [G   (vec (repeat N []))
         idx 1]
     (if (= idx N)
       G
       (let [[i j] (map dec (get-ints))]
         (recur
           (update-in (update-in G [i] (fn [x] (conj x j)))
             [j] (fn [x] (conj x i)))
           (inc idx))))))


(defn straigthen-tree
  "Since we got pairs in the tree, but didn't know the order, now need
   to remove ancestors from the neighbors of the tree.  So straightening
   the tree results in a tree where the values in the nbd map are only
   the children, not the parent."
  ([nbd]
    (straigthen-tree nbd 0 []))
  ([nbd root seen]
    (let [children (remove (set seen) (get nbd root))
          new-seen (conj seen root)]
      (if (empty? children)
        (assoc nbd root [])
        (assoc
          (reduce (fn [acc r] (straigthen-tree acc r new-seen)) nbd children)
          root children)))))

(defn encode-nodes
  "Create a map where the value of a node is the path from the root to
   get to it."
  ([nbd]
    (encode-nodes nbd 0 [0] {}))
  ([nbd root path m]
    (let [children (get nbd root)]
      (merge
        {root path}
        (if (empty? children)
          {}
          (apply merge
            (map
              (fn [r] (encode-nodes nbd r (conj path r) m))
              children)))))))

(defn update-vals
  "Update the atoms in the vals vector related to descendants of root
   according to nbd by add-vals."
  [nbd root vals add-val]
  (swap! (get vals root) (fn [x] (+ x add-val)))
  (let [children (get nbd root)]
    (if (not (empty? children))
      (doseq [child children]
        (update-vals nbd child vals add-val)))))

(defn maxq
  "Get the max along the path from node i to node j (node i and j included)"
  [nn vals i j]
  (let [pi  (nn i)
        pj  (nn j)
        nodes (concat [i j] (remove (set pj) pi) (remove (set pi) pj) [(last (filter (set pi) pj))])]
    (apply max (map (fn [idx] (deref (get vals idx))) nodes))))

(defn execute-next
  [nbd nn vals]
  (let [[Q i j] (clojure.string/split (read-line) #" ")
        i       (long (Integer/parseInt i))
        j       (long (Integer/parseInt j))]
    (if (= "add" Q)
      (update-vals nbd (dec i) vals j)
      (println (maxq nn vals (dec i) (dec j))))))

;;(trace/trace-vars update-vals)

(defn main []
  (let [N       (get-int)
        nbd     (get-base-tree N)
        Qs      (get-int)
        nbd     (straigthen-tree nbd)
        nn      (encode-nodes nbd)
        vals    (mapv atom (repeat N (long 0)))]
  (doseq [i (range Qs)] (execute-next nbd nn vals))))
;;
(binding [*in* (java.io.BufferedReader. (java.io.FileReader. "testcase.txt"))]
 (main))
