import Shared
import SwiftUI

@main
struct iOSApp: App {
    init() {
        InitKoinKt.doInitKoin(config: nil)
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}