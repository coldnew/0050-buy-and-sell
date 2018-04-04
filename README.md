# 台股 0050 ETF 買賣追蹤系統

構想: 參考 [施昇輝](http://search.books.com.tw/search/query/key/%E6%96%BD%E6%98%87%E8%BC%9D/adv_author/1/) 的 0050 投資術，建立一個專門針對0050 ETF 的買賣提示頁面。

TODO

## 構想

- [ ] 基本資訊判讀 (KD, 大盤, K < 20 買, K > 80 賣)
- [ ] 基本網頁建立 (static site) -> 送到gh-page去
- [ ] admin panel ?
- [ ] 透過 Telegram Channel 發布訊息 (telegra.ph ?)
- [ ] 除息日通知
- [ ] 成分股變更通知
- [ ] 買賣通知

## 開發與執行

使用以下命令搭配 nrepl 開發, 會產生HTTP伺服器在 http://127.0.0.1:3000

``` shell
boot dev
```

產生目標 jar 檔案的話，則使用

``` shell
boot prod
```

## 0050 相關連結

- [台灣證券交易所 - 元大台灣卓越50基金](http://www.twse.com.tw/zh/ETF/fund/0050)

- [元大台灣卓越50基金 - 詳細基金成份股](http://www.p-shares.com/#/FundWeights/1066)

## License

Copyright © 2018 Yen-Chin, Lee <<coldnew.tw@gmail.com>>

Distributed under the [MIT License](http://opensource.org/licenses/MIT).
