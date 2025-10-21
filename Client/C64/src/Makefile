# Makefile converted from make.cmd, makeboot.cmd
# Targets: all (default), boot, clean, help

CC65_BIN := cc65
CA65 := ca65
LD65 := ld65
EXOMIZER := exomizer

SRC_DIR := $(abspath $(dir $(lastword $(MAKEFILE_LIST))))
OUT_DIR := $(abspath $(SRC_DIR)/../target)
TFTP_DIR := $(OUT_DIR)/tftpboot
COMMON := $(abspath $(SRC_DIR)/../../common)

ROGUE_BIN := rogue6000.bin
ROGUE_LA := $(OUT_DIR)/rogue6000-la.bin
BOOT_TEMP := rboottemp.prg
BOOT_PRG := rogueboot.prg

.PHONY: all boot clean help

all: $(ROGUE_LA) $(TFTP_DIR)/roguedata
	@$(MAKE) boot

# Assemble and link main program
$(ROGUE_BIN): rogue.s
	$(CA65) rogue.s
	$(LD65) rogue.o -C cfg/c64prg-rogue-6000.cfg ip65/ip65/ip65.lib ip65/drivers/c64rrnet.lib -m rogue6000.map -vm -o $@

# Prepend load address (binary concat)
$(ROGUE_LA): $(ROGUE_BIN) loadaddress6000.bbb | $(OUT_DIR)
	mkdir -p $(OUT_DIR)
	cat loadaddress6000.bbb $(ROGUE_BIN) > $@
	@echo Created $@

# Prepare tftpboot data by truncating font/empty files and concatenating with binary
$(TFTP_DIR)/roguedata: $(ROGUE_BIN) $(COMMON)/rogue-font.raw $(COMMON)/empty.raw | $(TFTP_DIR)
	mkdir -p $(TFTP_DIR)
	dd skip=2 count=2048 if=$(COMMON)/font/rogue-font.prg of=$(COMMON)/rogue-font.raw bs=1
	dd count=6144 if=/dev/zero of=$(COMMON)/empty.raw bs=1
	cat $(COMMON)/rogue-font.raw $(COMMON)/empty.raw $(ROGUE_BIN) > $@
	@echo Created $@

# Boot target (build the bootable PRG and pack with exomizer)
boot: $(BOOT_PRG)

$(BOOT_PRG): rogueboot.s
	$(CA65) rogueboot.s
	$(LD65) rogueboot.o -C cfg/c64prg-vortex-boot.cfg ip65/ip65/ip65.lib ip65/drivers/c64rrnet.lib -o $(BOOT_TEMP)
	# exomizer invocation mirrors Windows .exe call in original script; override EXOMIZER if needed
	$(EXOMIZER) sfx basic $(BOOT_TEMP) -o $@
	@echo Created $@
	cp -f $@ $(OUT_DIR)

$(OUT_DIR):
	mkdir -p $(OUT_DIR)

$(TFTP_DIR):
	mkdir -p $(TFTP_DIR)

clean:
	-@rm -f *.o *.bin *.prg *.map
	-@rm -f $(OUT_DIR)/rogue6000-la.bin
	-@rm -f $(OUT_DIR)/rogueboot.prg
	-@rm -f $(TFTP_DIR)/roguedata
	@echo Cleaned build artifacts

help:
	@echo "Available targets:"
	@echo "  all     - default: build program and tftp data"
	@echo "  boot    - build boot PRG and pack with exomizer"
	@echo "  test    - build and copy load-addressed binary to $(HOME)/rogue"
	@echo "  clean   - remove build artifacts"
