(set-env!
 :source-paths    #{"src/clj" "src/cljc" "src/cljs"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs          "2.0.0"      :scope "test"]
                 [adzerk/boot-cljs-repl     "0.3.3"      :scope "test"]
                 [adzerk/boot-reload        "0.5.2"      :scope "test"]
                 [pandeiro/boot-http        "0.8.3"      :scope "test"]
                 [com.cemerick/piggieback   "0.2.1"      :scope "test"]
                 [org.clojure/tools.nrepl   "0.2.13"     :scope "test"]
                 [weasel                    "0.7.0"      :scope "test"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.10.238"]
                 [ring "1.5.1"]
                 [clj-http "3.8.0"]
                 [hickory "0.7.1"]
                 [cheshire "5.8.0"]])

(task-options!
 pom {:project 'tw0050
      :version "1.0.0-SNAPSHOT"
      :description "A simple 0050.tw buy and sell notifier system"}
 aot {:namespace '#{tw0050.core}}
 jar {:main 'tw0050.core}
 sift {:include #{#"\.jar$"}})

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 ;; '[pandeiro.boot-http    :refer [serve]]
 'tw0050.core)


(deftask dev
  "The `dev` task wraps the building of your application in some
   useful tools for local development: an http server, a file watcher
   a ClojureScript REPL and a hot reloading mechanism"
  []
  (comp (with-pass-thru _
       (tw0050.core/dev-main))
     (speak)
     (watch)
     (reload :asset-path "public")
     (cljs-repl :nrepl-opts {:port 9009})
     (cljs :source-map true :optimizations :none)
     (target)))

(deftask prod []
  "The task `prod` contains all necessary steps to produce production build."
  (comp
   (cljs :optimizations :advanced)
   (aot)
   (pom)
   (uber)
   (jar)
   (sift)
   (target)))


