extern crate proc_macro;
use proc_macro::TokenStream;

#[proc_macro_attribute]
pub fn opcode(_attr: TokenStream, item: TokenStream) -> TokenStream {
    item
}

#[proc_macro_attribute]
pub fn script_id(_attr: TokenStream, item: TokenStream) -> TokenStream {
    item
}

#[proc_macro_attribute]
pub fn first_arg_is_operand(_attr: TokenStream, item: TokenStream) -> TokenStream {
    item
}
