(ns tw0050.stock
  (:require  [tw0050.stock.getStockInfo :refer [getStockInfo]]))

(defn get0050Info []
  (getStockInfo "0050" "上市"))

#_(get0050Info)
