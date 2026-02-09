import Foundation
import SwiftUI

struct CustomStyle {
  enum Spacing: CGFloat {
    case tiny = 4
    case narrow = 8
    case small = 12
    case medium = 16
    case large = 20
    case wide = 24
    case extraWide = 30
    case ultraWide = 60
    case ultraWide2x = 120
  }

  enum CornerRadius: CGFloat {
    case small = 2
    case medium = 4
    case large = 8
  }

  enum FontSize: CGFloat {
    case small = 12
    case medium = 16
    case large = 20
  }

  enum Style {
    case largeSymbol
    case extraLargeSymbol
    case smallFont
    case mediumFont
    case playerProgressLoader
  }

  static let playerImageSize: CGFloat = 200
  static let playerMaxWidth: CGFloat = 350
  static let sideBarPlayerHeight: CGFloat = 550

  static func spacing(_ spacing: Spacing) -> CGFloat {
    spacing.rawValue
  }

  static func cornerRadius(_ radius: CornerRadius) -> CGFloat {
    radius.rawValue
  }

  static func fontSize(_ fontSize: FontSize) -> CGFloat {
    fontSize.rawValue
  }
}

extension View {
  @ViewBuilder func customStyle(_ style: CustomStyle.Style) -> some View {
    switch style {
    case .largeSymbol:
      font(.system(size: CustomStyle.spacing(.wide)))

    case .extraLargeSymbol:
      font(.system(size: CustomStyle.spacing(.extraWide)))

    case .smallFont:
      font(.system(size: CustomStyle.fontSize(.small)))

    case .mediumFont:
      font(.system(size: CustomStyle.fontSize(.medium)))

    case .playerProgressLoader:
      scaleEffect(0.6, anchor: .center)
        .frame(width: 10, height: 10)
    }
  }
}
