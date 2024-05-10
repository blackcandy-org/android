module Fastlane
  module Actions
    class GetVersionNameAction < Action
      def self.run(params)
        File.open("app/build.gradle", "r") do |file|
           version_name_line = file.find { |line| line.include?("versionName") }
           matched_data = version_name_line&.match(/versionName\s+"(.+)"/)

           matched_data[1] if matched_data
        end
      end

      def self.description
        "Get the version name"
      end

      def self.details
        "This action will return current version name"
      end

      def self.available_options
        []
      end

      def self.output
        [
          ['ANDROID_VERSION_NAME', 'The version name of your Android project']
        ]
      end

      def self.return_value
        "The version name"
      end

      def self.is_supported?(platform)
        platform == :android
      end
    end
  end
end
