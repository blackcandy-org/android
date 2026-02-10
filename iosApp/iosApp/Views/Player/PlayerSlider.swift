import SwiftUI

struct PlayerSliderView: UIViewRepresentable {
  @Binding var value: Double

  class Coordinator {
    @Binding var value: Double

    init(value: Binding<Double>) {
      _value = value
    }

    @objc func valueChanged(_ sender: UISlider) {
      value = Double(sender.value)
    }
  }

  func makeUIView(context: Context) -> UISlider {
    let slider = UISlider()

    slider.isContinuous = false
    slider.setThumbImage(
      .init(
        systemName: "circle.fill",
        withConfiguration: UIImage.SymbolConfiguration(pointSize: CustomStyle.fontSize(.small))
      ),
      for: .normal
    )

    slider.addTarget(
      context.coordinator,
      action: #selector(Coordinator.valueChanged(_:)),
      for: .valueChanged
    )

    return slider
  }

  func updateUIView(_ slider: UISlider, context: Context) {
    slider.value = Float(value)
  }

  func makeCoordinator() -> Coordinator {
    .init(value: $value)
  }
}
