(ns tw0050.stock.getStockInfo
  (:require [clj-http.client :as http]
            [hickory.core :as hickory]
            [hickory.select :as se]))

;; 

(defn- unix-timestamp
  "Create unix timestamp."
  []
  (System/currentTimeMillis))

(defn- fetch-data
  "Common function to retrive data."
  ([url] (fetch-data url {}))
  ([url params]
   (try
     (let [ret (http/get url (merge {:as :json
                                     :insecure? true
                                     ;; :debug true
                                     :socket-timeout 1000
                                     :conn-timeout 1000} params))
           body (ret :body)]
       ;; when success, it'll return "OK" on :rtmessage
       (if (= "OK" (:rtmessage body))
         (-> body :msgArray first)
         ;; throw exception when failed
         (throw (ex-info "fetch-data failed." {:url url :params params :ret ret}))))
     (catch Exception e    ; <= socketTimeout ?
       (println (.getMessage e))))))

(defn- get-cookies []
  (let [cs (clj-http.cookies/cookie-store)]
    ;; first retrive cookies data
    (fetch-data "http://mis.twse.com.tw/stock/api/getStockInfo.jsp"
                {:cookie-store cs
                 :query-params {:ex_ch "tse_t00.tw%7cotc_o00.tw%7ctse_FRMSA.tw"
                                :json 1
                                :delay 0
                                :_ (unix-timestamp)}})
    ;; return the cookie data
    cs))

#_(get-cookies)

(defn- translate-keys
  [coll]
  (-> coll
      (clojure.set/rename-keys
       {:c     :股票代號
        :s     :當盤成交量
        :ch    :channel
        :ex    :上市或上櫃                  ; tse: 上市, otc: 上櫃
        :n     :股票暱稱                    ; ex: 台泥
        :nf    :股票全名                    ; 台灣水泥股份有限公司
        :z     :最近成交價
        :tv    :當盤成交量                  ; temporal volume
        :v     :當日累計成交量              ; volume
        :a     :最佳五檔賣出價格
        :f     :最佳五檔賣出數量
        :b     :最佳五檔買入價格
        :g     :最佳五檔買入數量
        :tlong :資料時間                    ; :t (long)
        :t     :揭示時間
        :o     :開盤價
        :d     :今日日期
        :h     :今日最高
        :l     :今日最低
        :u     :漲停點
        :w     :跌停點
        :y     :昨收})))

;; 
;; Exported functions

(defn getStockInfo
  "Get stock info according to stock-id"
  ([stock-id type] (getStockInfo stock-id type (get-cookies)))
  ([stock-id type cookies]
   (let [ex_ch (str (case type
                      "上市" "tse_"
                      :tse   "tse_"
                      "上櫃" "otc_"
                      :otc   "otc_"
                      (throw (ex-info "Wrong type" {:stock-id stock-id :type type})))
                    stock-id ".tw")]
     ;; we use the cookies data to get the stock info
     (if-let [ret (fetch-data "http://mis.twse.com.tw/stock/api/getStockInfo.jsp"
                              {:cookie-store cookies
                               :query-params {:ex_ch ex_ch
                                              :json 1
                                              :delay 0
                                              :_ (unix-timestamp)}})]
       (translate-keys ret)
       (throw (ex-info "getStockInfo failed." {:stock-id stock-id}))))))

;; for test
#_(getStockInfo 2330 "上市")
#_(getStockInfo "0050" "上市")
#_(getStockInfo 6488 "上櫃")
