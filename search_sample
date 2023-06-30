####################################################################
## This is subject to change
####################################################################

host=$1

curl -XPUT $host'/search' -H 'Content-Type: application/json' -d'
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 2,
    "analysis": {
      "analyzer": {
        "custom_analyzer_with_edge_ngram_filter": {
          "tokenizer": "standard",
          "char_filter": [
            "html_strip"
          ],
          "filter": [
            "lowercase",
            "asciifolding",
            "trim",
            "classic",
            "unique",
            "custom_edge_ngram_filter"
          ]
        }
      },
      "filter": {
        "custom_edge_ngram_filter": {
          "type": "edge_ngram",
          "min_gram": 2,
          "max_gram": 10
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "id": {
        "type": "long",
        "fields": {
          "keyword": {
            "type": "keyword"
          }
        }
      },
      "name": {
        "type": "keyword",
        "fields": {
          "edge_ngram_filter": {
            "type": "text",
            "analyzer": "custom_analyzer_with_edge_ngram_filter",
            "search_analyzer": "standard"
          },
          "standard": {
            "type": "text"
          }
        }
      },
      "instrument": {
        "type": "keyword"
      },
      "instrument_type": {
        "type": "keyword"
      },
      "co_code": {
        "type": "keyword",
        "fields": {
          "edge_ngram_filter": {
            "type": "text",
            "analyzer": "custom_analyzer_with_edge_ngram_filter",
            "search_analyzer": "standard"
          },
          "standard": {
            "type": "text"
          }
        }
      },
      "company_name": {
        "type": "keyword",
        "fields": {
          "edge_ngram_filter": {
            "type": "text",
            "analyzer": "custom_analyzer_with_edge_ngram_filter",
            "search_analyzer": "standard"
          },
          "standard": {
            "type": "text"
          }
        }
      },
      "company_short_name": {
        "type": "keyword",
        "fields": {
          "edge_ngram_filter": {
            "type": "text",
            "analyzer": "custom_analyzer_with_edge_ngram_filter",
            "search_analyzer": "standard"
          },
          "standard": {
            "type": "text"
          }
        }
      },
      "category_name": {
        "type": "keyword",
        "fields": {
          "edge_ngram_filter": {
            "type": "text",
            "analyzer": "custom_analyzer_with_edge_ngram_filter",
            "search_analyzer": "standard"
          },
          "standard": {
            "type": "text"
          }
        }
      },
      "isin": {
        "type": "keyword",
        "fields": {
          "edge_ngram_filter": {
            "type": "text",
            "analyzer": "custom_analyzer_with_edge_ngram_filter",
            "search_analyzer": "standard"
          },
          "standard": {
            "type": "text"
          }
        }
      },
      "bse_group": {
        "type": "keyword",
        "fields": {
          "edge_ngram_filter": {
            "type": "text",
            "analyzer": "custom_analyzer_with_edge_ngram_filter",
            "search_analyzer": "standard"
          },
          "standard": {
            "type": "text"
          }
        }
      },
      "mcap": {
        "type": "double"
      },
      "mcap_type": {
        "type": "keyword",
        "fields": {
          "edge_ngram_filter": {
            "type": "text",
            "analyzer": "custom_analyzer_with_edge_ngram_filter",
            "search_analyzer": "standard"
          },
          "standard": {
            "type": "text"
          }
        }
      },
      "sector_code": {
        "type": "keyword",
        "fields": {
          "edge_ngram_filter": {
            "type": "text",
            "analyzer": "custom_analyzer_with_edge_ngram_filter",
            "search_analyzer": "standard"
          },
          "standard": {
            "type": "text"
          }
        }
      },
      "sector_name": {
        "type": "keyword",
        "fields": {
          "edge_ngram_filter": {
            "type": "text",
            "analyzer": "custom_analyzer_with_edge_ngram_filter",
            "search_analyzer": "standard"
          },
          "standard": {
            "type": "text"
          }
        }
      },
      "industry_code": {
        "type": "keyword",
        "fields": {
          "edge_ngram_filter": {
            "type": "text",
            "analyzer": "custom_analyzer_with_edge_ngram_filter",
            "search_analyzer": "standard"
          },
          "standard": {
            "type": "text"
          }
        }
      },
      "industry_name": {
        "type": "keyword",
        "fields": {
          "edge_ngram_filter": {
            "type": "text",
            "analyzer": "custom_analyzer_with_edge_ngram_filter",
            "search_analyzer": "standard"
          },
          "standard": {
            "type": "text"
          }
        }
      },
      "latest_nav": {
        "type": "double"
      },
      "latest_aum": {
        "type": "double"
      },
      "expense_ratio": {
        "type": "double"
      },
      "tracking_error": {
        "type": "double"
      },
      "benchmark_name": {
        "type": "keyword"
      },
      "sebi_category_name": {
        "type": "keyword"
      },
      "created_by": {
        "type": "keyword"
      },
      "updated_by": {
        "type": "keyword"
      },
      "subcategory": {
        "type": "keyword"
      },
      "siblings": {
        "type": "nested"
      },
      "suggest": {
        "type": "completion",
        "preserve_separators": false,
        "analyzer": "standard",
        "search_analyzer": "standard",
        "contexts": [
          {
            "name": "exchange_instrument_type_enabled",
            "type": "category"
          }
        ]
      },
      "query_string": {
        "type": "keyword",
        "fields": {
          "edge_ngram_filter": {
            "type": "text",
            "analyzer": "custom_analyzer_with_edge_ngram_filter",
            "search_analyzer": "standard"
          },
          "standard": {
            "type": "text"
          }
        }
      },
      "exchange": {
        "type": "keyword"
      },
      "segment": {
        "type": "keyword"
      },
      "security_id": {
        "type": "keyword"
      },
      "symbol": {
        "type": "keyword"
      },
      "symbol_name": {
        "type": "keyword"
      },
      "series": {
        "type": "keyword"
      },
      "stenabledtus": {
        "type": "keyword"
      },
      "exch_stenabledtus": {
        "type": "keyword"
      },
      "preopen_flag": {
        "type": "keyword"
      },
      "preopen_exch_flag": {
        "type": "keyword"
      },
      "face_value": {
        "type": "double"
      },
      "tick_size": {
        "type": "double"
      },
      "lot_size": {
        "type": "integer"
      },
      "upper_limit": {
        "type": "double"
      },
      "lower_limit": {
        "type": "double"
      },
      "update_date": {
        "type": "date"
      },
      "exch_symbol": {
        "type": "keyword"
      },
      "freeze_quantity": {
        "type": "integer"
      },
      "updated_at": {
        "type": "date",
        "format": "dd-MM-yyyy HH:mm:ss"
      },
      "short_description": {
        "type": "text"
      },
      "long_description": {
        "type": "keyword",
        "index": false
      },
      "constituents": {
        "type": "long",
        "index": true
      },
      "exchange_name": {
        "type": "keyword"
      },
      "bcast_name": {
        "type": "keyword"
      },
      "expiry_type": {
        "type": "keyword"
      },
      "expiry_display_date": {
        "type": "keyword"
      },
      "underlying_scrip_name": {
        "type": "keyword"
      },
      "strike_price": {
        "type": "double"
      },
      "option_type": {
        "type": "keyword"
      },
      "lot_units": {
        "type": "keyword"
      },
      "expiry_date": {
        "type": "date",
        "format": "dd-MM-yyyy HH:mm:ss"
      },
      "underlaying_scrip_code": {
        "type": "integer"
      },
      "one_month_performance": {
        "properties": {
          "fc": {
            "type": "float"
          },
          "lc": {
            "type": "float"
          },
          "percentage": {
            "type": "float"
          },
          "lc_date": {
            "type": "date",
            "format": "dd-MM-yyyy HH:mm"
          },
          "fc_date": {
            "type": "date",
            "format": "dd-MM-yyyy HH:mm"
          }
        }
      },
    "one_year_performance": {
      "properties": {
        "fc": {
          "type": "float"
        },
        "lc": {
          "type": "float"
        },
        "percentage": {
          "type": "float"
        },
        "lc_date": {
          "type": "date",
          "format": "dd-MM-yyyy HH:mm"
        },
        "fc_date": {
          "type": "date",
          "format": "dd-MM-yyyy HH:mm"
        }
      }
    },
    "three_year_performance": {
      "properties": {
        "fc": {
          "type": "float"
        },
        "lc": {
          "type": "float"
        },
        "percentage": {
          "type": "float"
        },
        "lc_date": {
          "type": "date",
          "format": "dd-MM-yyyy HH:mm"
        },
        "fc_date": {
          "type": "date",
          "format": "dd-MM-yyyy HH:mm"
        }
      }
    },
      "aggregate_vol": {
        "properties": {
          "weekly_vol_aggr": {
            "type": "long"
          },
          "weekly_del_vol_aggr": {
            "type": "long"
          },
          "quarterly_vol_aggr": {
            "type": "long"
          },
          "quarterly_del_vol_aggr": {
            "type": "long"
          }
        }
      },
      "bond_type": {
        "type": "keyword",
        "fields": {
          "edge_ngram_filter": {
            "type": "text",
            "analyzer": "custom_analyzer_with_edge_ngram_filter",
            "search_analyzer": "standard"
          },
          "standard": {
            "type": "text"
          }
        }
      },
      "bond_name": {
        "type": "keyword",
        "fields": {
          "edge_ngram_filter": {
            "type": "text",
            "analyzer": "custom_analyzer_with_edge_ngram_filter",
            "search_analyzer": "standard"
          },
          "standard": {
            "type": "text"
          }
        }
      },
      "bond_issuer_name": {
        "type": "keyword",
        "fields": {
          "edge_ngram_filter": {
            "type": "text",
            "analyzer": "custom_analyzer_with_edge_ngram_filter",
            "search_analyzer": "standard"
          },
          "standard": {
            "type": "text"
          }
        }
      },
      "bond_issuer_code": {
        "type": "keyword",
        "fields": {
          "edge_ngram_filter": {
            "type": "text",
            "analyzer": "custom_analyzer_with_edge_ngram_filter",
            "search_analyzer": "standard"
          },
          "standard": {
            "type": "text"
          }
        }
      },
      "fv": {
        "type": "double"
      },
      "date_of_allotment": {
        "type": "keyword"
      },
      "redemption_date": {
        "type": "keyword"
      },
      "coupon_rate": {
        "type": "double"
      },
      "credit_rating": {
        "type": "keyword"
      },
      "effective_yield": {
        "type": "double"
      }
    }
  }
}'
