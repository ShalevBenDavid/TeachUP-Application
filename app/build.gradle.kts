plugins {
	id("com.android.application")
	id("com.google.gms.google-services")
	id("org.jetbrains.kotlin.android")
}

android {
	namespace = "com.example.login2"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.example.login2"
		minSdk = 24
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		multiDexEnabled = true
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildFeatures {
		viewBinding = true
		compose = true
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
		isCoreLibraryDesugaringEnabled = true
	}

	kotlinOptions {
		jvmTarget = "1.8"
	}

	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.1"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}

}

dependencies {

	implementation("androidx.appcompat:appcompat:1.6.1")
	implementation("com.google.android.material:material:1.11.0")
	implementation("androidx.constraintlayout:constraintlayout:2.1.4")
	implementation("com.google.firebase:firebase-auth:22.3.1")
	implementation("com.google.firebase:firebase-database:20.3.0")
	implementation("com.google.firebase:firebase-firestore:24.10.2")
	implementation("com.google.firebase:firebase-storage:20.3.0")
	implementation("com.google.firebase:firebase-analytics")
	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	implementation("com.google.android.gms:play-services-auth:20.7.0")
	implementation("com.firebaseui:firebase-ui-firestore:8.0.2")
	implementation("de.hdodenhof:circleimageview:3.1.0")
	implementation("com.github.bumptech.glide:glide:4.12.0")
	coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
	implementation("androidx.activity:activity-compose:1.8.2")
	implementation("androidx.recyclerview:recyclerview:1.3.2")
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material:material-icons-extended")
	implementation("androidx.compose.material3:material3")
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
	implementation("androidx.navigation:navigation-compose:2.7.7")
	implementation("androidx.compose.material:material-icons-extended")
	implementation("androidx.compose.material3:material3")
	implementation(platform("androidx.compose:compose-bom:2023.08.00"))
	implementation("androidx.core:core-ktx:1.12.0")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

}