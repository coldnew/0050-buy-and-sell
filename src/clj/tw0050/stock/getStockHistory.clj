(ns tw0050.stock.getStockHistory
  (:require [clj-http.client :as http]
            [hickory.core :as hickory]
            [hickory.select :as se]))

;; 

(defn html->hickory [html-str]
  (-> html-str
      hickory/parse
      hickory/as-hickory))

(defn- fetch-data
  "Common function to retrive data."
  ([url] (fetch-data url {}))
  ([url params]
   (try
     (let [ret (http/get url (merge {:insecure? true
                                     ;; :debug true
                                     :socket-timeout 1000
                                     :conn-timeout 1000} params))]
       ;; return the body if success
       (ret :body))
     (catch Exception e                 ; <= socketTimeout ?
       (println (.getMessage e))))))

;; http://www.twse.com.tw/exchangeReport/STOCK_DAY?response=html&date=20180406&stockNo=0050

;; 移除在coll 裡面的 set 的東西
(defn remove-contains [s coll]
  (filter #(not (or (map? %)
                    (contains? s %))) coll))

;; 
;; Exported functions

;; FIXME: only support twse, not otc
(defn getStockHistory
  "Get stock history data"
  [stock-id date]
  (if-let [ret (fetch-data
                "http://www.twse.com.tw/exchangeReport/STOCK_DAY"
                {:query-params {:stockNo stock-id
                                :response "html"
                                :date date}})]
    (->> (se/select
          (se/child (se/tag :td))  (html->hickory ret))
         (map #(-> % :content last))
         (remove-contains #{"日期"
                            "成交股數"
                            "成交金額"
                            "開盤價"
                            "最高價"
                            "最低價"
                            "收盤價"
                            "漲跌價差"
                            "成交筆數"})
         (partition 9)
         (mapv #(zipmap [:日期 :成交股數 :成交金額 :開盤價 :最高價 :最低價 :收盤價 :漲跌價差 :成交比數] %)))

    (throw (ex-info "getStockHostory failed." {:stock-id stock-id}))))


#_(getStockHistory "0050" "20180406")
