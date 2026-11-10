# PulsePlatform とは

PulsePlatform は、AI エージェントでデスクトップアプリとモバイルアプリを作るためのフレームワークです。エージェントに適切なコードを書かせ、アイデアをアプリの形にし、アプリが大きくなってもその形を保つことを目指しています。ルールとライブラリの 2 つでできていて、ルールはプロジェクトが実行するテスト、ライブラリはそのルールが求める部品です。技術基盤は Compose Multiplatform で、デスクトップアプリは JVM で動き、Android と iOS のアプリは Kotlin のコードを共有します。

## アイデアからアプリへ

あなたはアプリで何をしたいかを伝えます。エージェントはライブラリの部品でそれを書き、ルールをまとめた 1 本のテストを実行し、失敗が示す箇所を直して、テストが通るまで繰り返します。あなたがレビューするのは、すでに決めた形になっているコードです。次のアイデアも、誰が書いても同じ形の上に作られます。この流れの準備は [AI エージェントと作る](/ja/guide/ai-agents) で説明しています。

## なぜルールが先なのか

AI エージェントは、コンパイルが通り、最初から動く画面を書きます。自分では保てないのは、そのまわりのコードの形です。ある画面は Section で ViewModel を結びつけ、別の画面は State の隣に `var` を置き、また別の画面は domain に文言を置きます。どの選択もそれ自体は動くので、コードベースは少しずつずれていきます。

プロンプトやスタイルガイドに書いたルールは読み飛ばされます。失敗するテストになったルールはそうはいきません。PulsePlatform のルールは [Konsist](https://docs.konsist.lemonappdev.com/) のアサートで、自分のテストスイートの 1 本のテストから呼びます。コードがずれるとテストが失敗し、ファイルの名前と、何をどこへ動かすかを示します。エージェントはそのメッセージを読み、コンパイルエラーと同じようにコードを直します。

もう半分がライブラリです。ルールは、ViewModel を結びつける場所が 1 つであること、Section と Component がデータとコールバックだけを受け取ること、State が 1 つの不変な値であることを求めます。ライブラリはそれをいちばん書きやすい画面の書き方にするので、ルールに従うために余計なコードを書く必要はありません。

## ルールが扱うもの

| 範囲 | 検査する内容 |
|---|---|
| [UI の構成](/ja/guide/rules/ui) | feature を、ViewModel を結びつける場所と Section・Component にどう分けるか、State とメッセージ、Preview |
| [ドメインとデータ](/ja/guide/rules/domain) | Repository がデータをどう公開するか、UseCase を書く価値があるのはどんなときか |
| [モジュールと Koin](/ja/guide/rules/modules) | どのモジュールがどのモジュールに依存してよいか、その中のパッケージ、Koin のモジュールをどこで定義するか |
| [文言](/ja/guide/rules/text) | ユーザーが読む文字列がすべて文字列リソースから来ていて、アプリを翻訳できること |

## デスクトップとモバイル

PulsePlatform で作るデスクトップアプリとモバイルアプリは、それぞれの使われ方に合わせた別のプロダクトです。共有するのは domain と data だけで、画面は共有しません。両方を作るアプリは `desktop/` と `mobile/` に分けて置きます。詳しくは [デスクトップとモバイル](/ja/guide/rules/modules#デスクトップとモバイル) を見てください。ルールとコアはどのプラットフォームでも同じです。`PulseViewModel` は 1 つの不変な State を持ち、Action を処理して Event を発行し、`PulseContent` がそれを UI に結びつけます。違うのは、1 つの画面に ViewModel がいくつあるかです。

デスクトップのウィンドウには、サイドバー、エディタ、インスペクタが同時に並びます。それぞれが自分の ViewModel を持つ **Content** で、**Host** がそれを並べ、その上の **Container** が Content の間でメッセージを運びます。スマートフォンは一度に 1 つの画面しか表示しないので、**Screen** が ViewModel を 1 つ持って自分で結びつけ、Container はありません。

| | デスクトップ | モバイル |
|---|---|---|
| アーティファクト | `mvi-desktop` | `mvi-mobile` |
| ターゲット | JVM（Compose Desktop） | Android、iOS（Compose Multiplatform） |
| feature の単位 | Host、その Content、Container | Screen 1 つと ViewModel 1 つ |
| ViewModel を結びつける場所 | 各 Content | Screen |
| ViewModel 間のメッセージ | Container を通る Broadcast と Unicast | なし |
| ルール | `assertPulseMviConventions()` | `assertPulseMobileConventions()` |

```
デスクトップ                             モバイル
counter/                                 counter/
├── CounterHost.kt                       ├── CounterScreen.kt
├── CounterContainer.kt                  ├── CounterViewModel.kt
├── CounterContracts.kt                  ├── CounterContracts.kt
├── content/                             ├── section/
│   └── total/                           └── component/
│       ├── TotalContent.kt
│       ├── TotalViewModel.kt
│       ├── TotalContracts.kt
│       └── section/
├── section/
└── component/
```

## アーティファクト

PulsePlatform は JitPack で 5 つのアーティファクトとして公開しています。ルールはどのプラットフォームでも使い、MVI と Navigation 3 のアーティファクトはターゲットに合わせて選びます。

| アーティファクト | 役割 | プラットフォーム |
|---|---|---|
| `architecture` | ルール。テストから呼ぶ Konsist のアサート | JVM で動き、デスクトップとモバイルのコードを検査する |
| `mvi-desktop` | `PulseViewModel`、`PulseContainer`、`PulseHost`、`PulseContent`、5 つのマーカーインターフェース | デスクトップ |
| `mvi-mobile` | `PulseViewModel`、`PulseContent`、3 つのマーカーインターフェース | Android、iOS |
| `navigation-desktop` | ViewModel と Container を Navigation 3 のバックスタックエントリに結びつける | デスクトップ |
| `navigation-mobile` | ViewModel を Navigation 3 のバックスタックエントリに結びつける | Android、iOS |

## インストール

`settings.gradle.kts` に JitPack を追加し、`build.gradle.kts` にプラットフォームに合ったアーティファクトを追加します。`<version>` は [GitHub Releases](https://github.com/kaleidot725/PulsePlatform/releases) のタグに置き換えてください。

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

```kotlin
// build.gradle.kts — デスクトップ
dependencies {
    implementation("com.github.kaleidot725.PulsePlatform:mvi-desktop:<version>")
    implementation("com.github.kaleidot725.PulsePlatform:navigation-desktop:<version>")
    testImplementation("com.github.kaleidot725.PulsePlatform:architecture:<version>")
}
```

```kotlin
// build.gradle.kts — モバイル（Compose Multiplatform）
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("com.github.kaleidot725.PulsePlatform:mvi-mobile:<version>")
            implementation("com.github.kaleidot725.PulsePlatform:navigation-mobile:<version>")
        }
    }
}

dependencies {
    // ルールは Android のユニットテストで実行する
    testImplementation("com.github.kaleidot725.PulsePlatform:architecture:<version>")
}
```

## 要件

| 要件 | バージョン |
|---|---|
| Java | 17+ |
| Kotlin | 2.0+ |
| Compose Multiplatform | 1.6+ |
| Android | `minSdk` 26 |

## 次のステップ

- [デスクトップのクイックスタート](/ja/guide/getting-started) — Host、Content、Container を持つカウンターのウィンドウ
- [モバイルのクイックスタート](/ja/guide/getting-started-mobile) — Android と iOS のカウンター画面
- [AI エージェントと作る](/ja/guide/ai-agents) — エージェントが自分の作業を確かめられるようにルールを用意する
- [ルール](/ja/guide/rules/) — すべてのルールとその理由
