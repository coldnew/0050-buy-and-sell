(ns tw0050.stock.datetime)

(defn unix-timestamp
  "Return unix timestamp."
  []
  (System/currentTimeMillis))