#!/bin/sh
bindgen=bindgen/target/scala-2.11/bindgen-out

out=./out
root=/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator.sdk
f=$root/System/Library/Frameworks/UIKit.framework/Headers
clangArgs="-W -isysroot -W $root"

mkdir -p $out

## UIApplication
#$bindgen -t objc -P cocoa.uikit -o $out/UIApplication.scala $clangArgs $f/UIApplication.h $@

## UIViewController
#$bindgen -t objc -P cocoa.uikit -o $out/UIViewController.scala $clangArgs $f/UIViewController.h $@

# UIResponder
$bindgen -t objc -P cocoa.uikit -o $out/UIResponder.scala $clangArgs $f/UIResponder.h $@

