# pivot

The library provides the ability to convert data structures to summarize,
analyze, explore and present your data.

## Usage

Our data set consists of 10 records and 6 fields. Order ID, Product,
Category, Amount, Date and Country.

```clojure
(def data
  [{:id 1 :product "Carrots" :category "Vegetables" :amount 4270 :date "1-6-2016" :country "United States"}
   {:id 2 :product "Broccoli" :category "Vegetables" :amount 8239 :date "1-7-2016" :country "United Kingdom"}
   {:id 3 :product "Banana" :category "Fruit" :amount 617 :date "1-8-2016" :country "United States"}
   {:id 4 :product "Banana" :category "Fruit" :amount 8384 :date "1-10-2016" :country "Canada"}
   {:id 5 :product "Beans" :category "Vegetables" :amount 2626 :date "1-10-2016" :country "Germany"}
   {:id 6 :product "Orange" :category "Fruit" :amount 3610 :date "1-11-2016" :country "United States"}
   {:id 7 :product "Broccoli" :category "Vegetables" :amount 9062 :date "1-11-2016" :country "Australia"}
   {:id 8 :product "Banana" :category "Fruit" :amount 6906 :date "1-16-2016" :country "New Zealand"}
   {:id 9 :product "Apple" :category "Fruit" :amount 2417 :date "1-16-2016" :country "France"}
   {:id 10 :product "Apple" :category "Fruit" :amount 7421 :date "1-16-2016" :country "Canada"}])
```
Library provides `pivot` function. `pivot` takes

 * List of filters
 * List of fields to grouping
 * Map with functions to compute values. Map's keys is a names, map's values is a functions

Let's count our fruits and vegetables:

```clojure
(pivot []
       [:category]
       data
       {:count count})
```

Result

```clojure
({:category "Vegetables", :count 4} {:category "Fruit", :count 6})
```

Let's count sum of amount by categories:

```clojure
(pivot []
       [:category]
       data
       {:amount (fn [x] (->> x (map :amount) (reduce +)))})
```

Result

```clojure
({:category "Vegetables", :amount 24197} {:category "Fruit", :amount 29355})
```

Another example.

 * Filter products with amount more than 1000
 * Group by category, then group by date
 * Let's count products and their sum of amount
 
```clojure
(pivot [(fn [x] (> (get x :amount) 1000))]
       [:category :date]
       data
       {:count count :amount (fn [x] (->> x (map :amount) (reduce +)))})
```

Result

```clojure
({:category "Vegetables", :date "1-6-2016", :count 1, :amount 4270}
 {:category "Vegetables", :date "1-7-2016", :count 1, :amount 8239}
 {:category "Vegetables", :date "1-10-2016", :count 1, :amount 2626}
 {:category "Vegetables", :date "1-11-2016", :count 1, :amount 9062}
 {:category "Fruit", :date "1-10-2016", :count 1, :amount 8384}
 {:category "Fruit", :date "1-11-2016", :count 1, :amount 3610}
 {:category "Fruit", :date "1-16-2016", :count 3, :amount 16744})
```

## License

Copyright © 2017 Valeriy Manenkov

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
