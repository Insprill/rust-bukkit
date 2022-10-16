[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![Apache License][license-shield]][license-url]




<!-- PROJECT LOGO -->
<br />
<div align="center">
  <h1>Rust Bukkit</h1>
  <p>
    Ever wanted to write Spigot plugins in Rust? Now you can.
    <br/>
    <br />
    <a href="https://github.com/Insprill/rust-bukkit/issues">Report Bug</a>
    ·
    <a href="https://github.com/Insprill/rust-bukkit/issues">Request Feature</a>
  </p>
</div>




<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#about-the-project">About The Project</a></li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#building">Building</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
  </ol>
</details>




<!-- ABOUT THE PROJECT -->
## About The Project

Rust Bukkit is an experimental project to allow you to write Spigot plugins in Rust.
This library is still very early in development and lacking in many features. 
Breaking changes may occur at any time.
Any contributions are welcome!




<!-- USAGE -->
## Usage

#### The Java Side
To get started, follow the [build](#building) instructions to get a local copy of everything.
This library isn't published to Maven Central or crates.io yet due to its very premature, and experimental nature.

On the Java side, add the compiled `.jar` to your project.  
In your main class, instead of extending `JavaPlugin`, you'll extend `RustPlugin`.
When extending from `RustPlugin`, you'll need to override two additional methods.

The first being `#libraryName`, the second being `#enable`.

The `#libraryName` method must return the file name of your native library, without an extension (e.g. `test_plugin.dll` will return `test_plugin`).

The `#enable` method must be overridden with the following: 
```java
@Override
public native void enable();
```
There is also a `#disable` method which can be overridden if desired.

If you override the `#onEnable` method, **make sure to call it's super!**
If you don't, the native library won't be properly loaded and your Rust code won't run.


#### The Rust Side
In your `Cargo.toml`, append the following to the end of the file: 
```toml
[lib]
crate_type = ["cdylib"]
```
This tells rust to compile your library as a shared library.

You'll also need to add the [jni crate](https://crates.io/crates/jni) and `rust-bukkit` as dependencies.  
For `rust-bukkit`, you'll need to set `path` to the directory of the project you cloned earlier.

In your `lib.rs`, the entry point is the enable method. This method must be written in a specific way so the JVM can find it.
```rust
#[no_mangle]
pub extern "system" fn Java_my_package_MyClass_enable(env: JNIEnv, obj: JObject) {
}
```
`my_package` and `MyClass` are the package and class name of your main class, with the dots replaced with underscores.
If you're unsure what the method name should be, you can run your Java compiler with the `-h` flag and view the C header file that's generated and copy the method name from there.

The `disable` method must be written the same way.




<!-- BUILDING -->
## Building

To compile Rust Bukkit, you will need an internet connection, a terminal and [Cargo](https://rustup.rs/).  
Clone this repo, then run `./gradlew build`.  
You can find the compiled library jar in the `build/libs` directory, and the Rust crate can be found in the `rust` directory.




<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create.  
Any contributions you make are **greatly appreciated**!  
If you're new to contributing to open-source projects, you can follow [this](https://docs.github.com/en/get-started/quickstart/contributing-to-projects) guide.




<!-- LICENSE -->
## License

Distributed under the Apache 2.0 license.  
See [LICENSE][license-url] for more information.




<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/Insprill/rust-bukkit.svg?style=for-the-badge
[contributors-url]: https://github.com/Insprill/rust-bukkit/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/Insprill/rust-bukkit.svg?style=for-the-badge
[forks-url]: https://github.com/Insprill/rust-bukkit/network/members
[stars-shield]: https://img.shields.io/github/stars/Insprill/rust-bukkit.svg?style=for-the-badge
[stars-url]: https://github.com/Insprill/rust-bukkit/stargazers
[issues-shield]: https://img.shields.io/github/issues/Insprill/rust-bukkit.svg?style=for-the-badge
[issues-url]: https://github.com/Insprill/rust-bukkit/issues
[license-shield]: https://img.shields.io/github/license/Insprill/rust-bukkit.svg?style=for-the-badge
[license-url]: https://github.com/Insprill/rust-bukkit/blob/master/LICENSE
