#!/bin/sh
bindgen=bindgen/target/scala-2.11/bindgen-out

out=./out
f=/System/Library/Frameworks/AppKit.framework/Headers

mkdir -p $out

# NSApplication
$bindgen -t objc -P cocoa.foundation -o $out/NSApplication.scala $f/NSApplication.h $@

