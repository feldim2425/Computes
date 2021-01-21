# Computes (Minecraft Mod) [WIP]

Computes is a Mod for Minecraft Forge (and maybe Fabric) that adds Computers in-game.

The Computers run on a WASM based runtime, giving them support for many programming languages like Rust, C++, C,
AssemblyScript, ... and much more.

## Considerations

One Consideration is that WASM is not human-readable, so the Admin should be able to disable direct WASM support and
only give players the option to run pre-uploaded WASM modules which could include script support (Plan: Support Lua per
default)

## Work-in-progress

The project is in its early stages, the code in this repo is subject to change (a lot). The first goal is just to have
some sort of terminal output and keyboard input to work on the VM. Optimizations and proper refactoring will be done
after the VM is ready.

## The Architecture

The current plan is to use [Wasmer](https://wasmer.io/) for the runtime. While Wasmer has a Java interface, it is not
capable enough to be used directly, so a custom Rust library will be added and interfaced via the JNI. One limitation of
the default provided Java interface is, that it does not allow the use of middleware, which is required to limit the
amount of processing power each computer can use.

Currently, support for Windows, Linux and MacOS is planned (no ARM support). Support for a system has to be added to the
VM since it will run using native libraries.

It is planned to add persistence to the VM.
