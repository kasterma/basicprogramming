(ns stree.core)

(defn range-without [k i]
  (concat (range i) (range (inc i) k)))

(defn complete-graph [k]
  (zipmap (range k)
          (map (fn [i] (range-without k i)) (range k))))

(defn spanningtree
  ([gr]
     (let [boundary (first (keys gr))]
       (spanningtree gr [boundary] {} [])))
  ([gr [bn & bmore] tree nodes]
     ;; gr the graph
     ;; bn & bmore the search boundary
     ;; tree the tree build so far
     ;; nodes the nodes we want to add to the tree, but have not yet
     ;;   since we might want to add them with neighbors
     (if (not (nil? bn))
       (let [treev (into (keys tree) nodes)
             bnbds (gr bn)
             treen (remove (fn [n] (contains? (set treev) n))
                           bnbds)]
         (recur gr (into bmore treen) (assoc tree bn treen) (into nodes treen)))
       (let [no-nbd-nodes  (remove (fn [n] (contains? (set (keys tree)) n)) nodes)]
         (merge tree (zipmap no-nbd-nodes (repeat (count no-nbd-nodes) (list))))))))

(def g1
  {1 '(2 3)
   2 '(1 3)
   3 '(4 2 1)
   4 '(5 3)
   5 '(4 6)
   6 '(5 7 8)
   7 '(6 8)
   8 '(7 6)})
