(ns pivot.core)

(defn- apply-fns
  [fns coll]
  (->> fns
       (map #(% coll))))
    
(defn- apply-filters
  [filters coll]
  (loop [filters filters
         coll coll]
       (if (empty? filters)
           coll
           (recur (rest filters)
                  (filter (first filters) coll)
                  ))))

(defn- map-vals
  [m f]
  (zipmap (keys m) (map f (vals m))))

(defn- nested-group-by
  [fs coll & [final-fn]]
  (if (empty? fs)
    ((or (partial apply-fns final-fn) identity) coll)
    (map-vals (group-by (first fs) coll)
                              #(nested-group-by (rest fs) % final-fn))))

(defn- flatten-keys [m]
  (if (not (map? m))
    {[] m}
    (into {}
          (for [[k v] m
                [ks v'] (flatten-keys v)]
            [(cons k ks) v']
          ))))

(defn- flatten-data [m ks]
  (->> (seq (flatten-keys m))
       (map #(concat (first %) (second %)))
       (map (partial zipmap ks))
       ))

(defn pivot [filters fs final-fn coll]
  (flatten-data (nested-group-by fs
                   (apply-filters filters coll)
                   (vals final-fn)) (concat fs (keys final-fn))
		   ))
