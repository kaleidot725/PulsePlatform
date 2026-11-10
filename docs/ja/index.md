---
layout: home

hero:
  name: PulsePlatform
  text: AI エージェントでデスクトップアプリとモバイルアプリを作る
  tagline: AI エージェントに適切なコードを書かせ、アイデアをアプリの形にするためのフレームワーク。技術基盤は Compose Multiplatform です。
  actions:
    - theme: brand
      text: PulsePlatform とは
      link: /ja/guide/
    - theme: alt
      text: デスクトップのクイックスタート
      link: /ja/guide/getting-started
    - theme: alt
      text: モバイルのクイックスタート
      link: /ja/guide/getting-started-mobile

features:
  - icon: 💡
    title: アイデアからアプリへ
    details: 作りたいものを伝えれば、エージェントがデスクトップアプリ、Android と iOS のアプリ、またはその両方として作ります。コードをどこに置くかはフレームワークが決めるので、エージェントはアイデアの実現に集中できます。
  - icon: 🤖
    title: AI エージェントに適切なコードを
    details: エージェントはコードを書き、1 本のテストを実行し、何をどこへ動かすかを読みます。「アーキテクチャに従って」を通すべき検査に変えるので、アプリが大きくなっても形が崩れません。
  - icon: ✅
    title: 実行できるルール
    details: UI の構成、State、Repository、モジュール間の依存、Koin、文言を、自分のテストスイートの 1 本のテストで検査します。失敗するとファイルの名前と直し方が示されます。
  - icon: 🧩
    title: どの画面も同じ形に
    details: Section と Component はデータとコールバックだけを受け取り、ViewModel を結びつける場所は 1 つです。ルールが求める部品をライブラリが持っているので、正しい構成がいちばん書きやすい構成になります。
  - icon: 🪟
    title: デスクトップとモバイルは別のプロダクト
    details: デスクトップのウィンドウにはいくつものパネルが並び、Container がその間でメッセージを運びます。スマートフォンは 1 つの画面に ViewModel が 1 つです。両者が共有するのは domain と data で、画面は共有しません。
  - icon: 🛠️
    title: Compose Multiplatform が基盤
    details: すべて Kotlin と Compose で書きます。デスクトップは JVM で、モバイルは Android と iOS で動きます。State・Action・Event による MVI で、どのプラットフォームでも状態は一方向に流れます。
---
