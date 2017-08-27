#!/bin/sh
bindgen=bindgen/target/scala-2.11/bindgen-out

out=./out
f=/System/Library/Frameworks/AppKit.framework/Headers

mkdir -p $out

# NSEvent
$bindgen -t objc -P cocoa.foundation -o $out/NSEvent.scala $f/NSEvent.h $@

## NSResponder
#$bindgen -t objc -P cocoa.foundation -o $out/NSResponder.scala $f/NSResponder.h $@

## NSApplication
#$bindgen -t objc -P cocoa.foundation -o $out/NSApplication.scala $f/NSApplication.h $@

## NSView
#$bindgen -t objc -P cocoa.foundation -o $out/NSView.scala $f/NSView.h $@

## NSControl
#$bindgen -t objc -P cocoa.foundation -o $out/NSControl.scala $f/NSControl.h $@

## NSTextField
#$bindgen -t objc -P cocoa.foundation -o $out/NSTextField.scala $f/NSTextField.h $@

## NSWindow
#$bindgen -t objc -P cocoa.foundation -o $out/NSWindow.scala $f/NSWindow.h $@
