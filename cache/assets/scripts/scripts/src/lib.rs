#![allow(
    dead_code,
    non_snake_case,
    non_upper_case_globals,
    non_camel_case_types,
    unused_mut,
    unused_macros
)]

pub mod constants;
#[macro_use]
pub mod types;

pub mod prelude {
    pub use super::constants::*;
    pub use super::types::*;
    pub use scripts_macro::*;
}

mod game_settings;
