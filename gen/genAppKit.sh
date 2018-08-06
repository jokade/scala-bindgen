#!/bin/sh
bindgen=../bindgen/target/scala-2.11/bindgen-out

out=./out
f=/System/Library/Frameworks/AppKit.framework/Headers

mkdir -p $out

## NSEvent
#$bindgen -t objc -P cocoa.foundation -o $out/NSEvent.scala $f/NSEvent.h $@

## NSResponder
#$bindgen -t objc -P cocoa.foundation -o $out/NSResponder.scala $f/NSResponder.h $@

## NSApplication
#$bindgen -t objc -P cocoa.foundation -o $out/NSApplication.scala $f/NSApplication.h $@

## NSView
#$bindgen -t objc -P cocoa.foundation -o $out/NSView.scala $f/NSView.h $@

## NSControl
#$bindgen -t objc -P cocoa.foundation -o $out/NSControl.scala $f/NSControl.h $@

## NSWindow
#$bindgen -t objc -P cocoa.foundation -o $out/NSWindow.scala $f/NSWindow.h $@

## NSTextField
#$bindgen -t objc -P cocoa.foundation -o $out/NSTextField.scala $f/NSTextField.h $@

## NSDatePicker
#$bindgen -t objc -P cocoa.foundation -o $out/NSDatePicker.scala $f/NSDatePicker.h $@

# NSText
#$bindgen -t objc -P cocoa.foundation -o $out/NSText.scala $f/NSText.h $@

## NSTextView
#$bindgen -t objc -P cocoa.foundation -o $out/NSTextView.scala $f/NSTextView.h $@

## NSComboBox
#$bindgen -t objc -P cocoa.foundation -o $out/NSComboBox.scala $f/NSComboBox.h $@

## NSLevelIndicator
#$bindgen -t objc -P cocoa.foundation -o $out/NSLevelIndicator.scala $f/NSLevelIndicator.h $@

## NSButton
#$bindgen -t objc -P cocoa.foundation -o $out/NSButton.scala $f/NSButton.h $@

## NSTableView
#$bindgen -t objc -P cocoa.foundation -o $out/NSTableView.scala $f/NSTableView.h $@

## NSTableColumn
#$bindgen -t objc -P cocoa.foundation -o $out/NSTableColumn.scala $f/NSTableColumn.h $@

# NSTableRowView
$bindgen -t objc -P cocoa.foundation -o $out/NSTableRowView.scala $f/NSTableRowView.h $@
