(ns core
  (:require [clojure.tools.logging :as log]
            [clojure.test :as test :refer [deftest is]]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.tools.trace :as trace]
            [criterium.core :as criterium]))

;; There are two main types of objects on this file, trees, denoted by t, t1,
;; t2, etc, and zippers, denoted by z, z1, z2, etc.
;;
;; The trees are numbers or maps with keywords as keys, and values are trees.
;;
;; The zippers are maps with keys :cur and :up.  :cur gives a tree and :up
;; gives a list of maps with :key and :siblings.  :key gives a keyword, :siblings a
;; tree.

;; Here is an example, t1 is a tree, (zipper t1) the zipper corresponding
;; to the tree.  Then some operations on the zipper are shown.
;;
;; t1
;;     {:e {:f 5}, :a {:b {:c 3, :d 4}}}
;; (zipper t1)
;;     {:cur {:e {:f 5}, :a {:b {:c 3, :d 4}}}, :up ()}
;; (down (zipper t1) :e)
;;     {:cur {:f 5}, :up ({:key :e, :siblings {:a {:b {:c 3, :d 4}}}})}
;; (down (down (zipper t1) :e) :f)
;;     {:cur 5, :up ({:key :f, :siblings {}} {:key :e, :siblings {:a {:b {:c 3, :d 4}}}})}
;; (up (down (down (zipper t1) :e) :f))
;;     {:cur {:f 5}, :up ({:key :e, :siblings {:a {:b {:c 3, :d 4}}}})}

;; We presume operations are only called when approriate (i.e. going sideways
;; only done with sibling an existing sibling.  In performance testing, only
;; want the core of the algorithm tested here.


;; Key zipper functions

(defn up [{:keys [cur up] :as z}]
  (let [[{:keys [key siblings] :as parent} & more-up]  up]
    {:cur (assoc siblings key cur) :up (if (not (nil? more-up)) more-up (list))}))

(defn down [{:keys [cur up] :as z} child-key]
  {:cur (get cur child-key)
   :up (cons {:key child-key :siblings (dissoc cur child-key)} up)})

(defn sideways [{:keys [cur up] :as z} sibling]
  (let [[{:keys [key siblings] :as parent} & more-up]  up]
    {:cur (get siblings sibling)
     :up (cons {:key sibling :siblings (assoc (dissoc siblings sibling) key cur)} more-up)}))

;; Helper functions

(defn children [z]
  (keys (:cur z)))

(defn siblings [{:keys [cur up] :as z}]
  (if (empty? up)
    []
    (vec (keys (:siblings (first up))))))

(defn get-cur [z]
  (:cur z))

;; Conversion between tree and zipper

(defn zipper [t]
  {:cur t :up (list)})

(defn tree [{cur :cur upp :up :as z}]
  (if (not (empty? upp))
    (recur (up z))
    cur))

;; Basic test

(def t1 {:a {:b {:c 3 :d 4}} :e {:f 5}})
(def z1 (zipper t1))

(deftest basics
  (is (= (children z1)
         '(:e :a)))
  (is (= (down z1 :a)
         {:cur {:b {:c 3, :d 4}}, :up '({:key :a, :siblings {:e {:f 5}}})}))
  (is (= (down (sideways (down z1 :a) :e) :f)
         {:cur 5, :up '({:key :f, :siblings {}} {:key :e, :siblings {:a {:b {:c 3, :d 4}}}})}))
  (is (= (siblings (down z1 :a))
         [:e]))
  (is (= (down (down z1 :a) :b)
         {:cur {:c 3, :d 4}, :up '({:key :b, :siblings {}} {:key :a, :siblings {:e {:f 5}}})}))
  (is (= (siblings (down (down z1 :a) :b))
         []))
  (is (= (get-cur (up (up (down (sideways (down z1 :a) :e) :f))))
         {:e {:f 5}, :a {:b {:c 3, :d 4}}}))
  (is (identical? (t1 :a) (get-cur (down z1 :a))))
  (is (not (identical? z1 (up (down z1 :a)))))
  (is (= z1 (up (down z1 :a)))))

;; some random testing

(def compound (fn [inner-gen]
                (gen/not-empty (gen/map gen/keyword inner-gen))))
(def scalar-tree (gen/recursive-gen compound gen/nat))

(nth (gen/sample scalar-tree) 4)

(gen/sample (gen/resize 10 scalar-tree) 2)

(defspec downup-is-id
  100
  (prop/for-all [t (gen/not-empty scalar-tree)]
                (let [k (first (keys t))]
                  (= t (get-cur (up (down (zipper t) k)))))))

(defn not-empty-tree->tree-with-child-chosen [t]
  (gen/tuple (gen/elements (keys t)) (gen/return t)))

(def scalar-tree-with-chosen-child
  (gen/bind (gen/not-empty scalar-tree)
            not-empty-tree->tree-with-child-chosen))

(gen/sample scalar-tree-with-chosen-child)

(defspec downup-rand-is-id
  100
  (prop/for-all [kt (gen/not-empty scalar-tree-with-chosen-child)]
                (let [[k t] kt]
                  (= t (get-cur (up (down (zipper t) k)))))))


;; Performance comparisons

;; (def width 10000)
;; (criterium/quick-bench (zipmap (map (comp keyword str) (range width)) (range width)))
;; (criterium/quick-bench (into {} (for [k (range width)] [(keyword (str k)) k])))
;; (def r (range width))
;; (criterium/quick-bench (zipmap (map (comp keyword str) r) r))
;; (criterium/quick-bench (into {} (for [k r] [(keyword (str k)) k])))
;; into quicker (guess b/c it using a transient datastruct internally)

(defn make-deep-tree
  "Make a tree with one path of length depth, followed by width branching."
  [depth width]
  (let [fm   (into {} (for [k (range width)] [(keyword (str k)) k]))
        nest (fn [m] {:k m})]
    (nth (iterate nest fm) depth)))

(def depth 10)
(def width 40)

(def t-deep (make-deep-tree depth width))

;; compare update-in inc all leaves with zipping down, inc all leaves, and
;; zipping back up

(def ks (vec (repeat depth :k)))
(def ls (vec (map (comp keyword str) (range width))))
(def addr (map (fn [l] (conj ks l)) ls))

(comment
  (time (reduce (fn [acc a] (update-in acc a inc)) t-deep addr)))

(defn zip-down-inc-up [t depth width ls]
  (let [z-deep (zipper t)
        z-down (nth (iterate (fn [z] (down z :k)) z-deep) depth)
        z-leaf (update-in (down z-down (first ls)) [:cur] inc)]
    (tree (reduce (fn [acc s] (update-in (sideways acc s) [:cur] inc))
                  z-leaf
                  (rest ls)))))

(comment
  (time (zip-down-inc-up t-deep depth width)))

(defn run-timings
  ([depth width]
     (run-timings depth width true true))
  ([depth width run-zip? run-update?]
     (let [t-deep (make-deep-tree depth width)
           ks     (vec (repeat depth :k))
           ls     (vec (map (comp keyword str) (range width)))
           addr   (map (fn [l] (conj ks l)) ls)]
       (if run-zip?
         (nil? (time (zip-down-inc-up t-deep depth width ls))))
       (if run-update?
         (nil? (time (reduce (fn [acc a] (update-in acc a inc)) t-deep addr)))))))






